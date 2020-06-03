package com.kronos.autoregister;

import com.android.SdkConstants;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.google.common.collect.ImmutableSet;
import com.kronos.autoregister.helper.Log;
import com.kronos.plugin.base.BaseTransform;
import com.kronos.plugin.base.ClassUtils;
import com.kronos.plugin.base.TransformCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class NewAutoRegisterTransform extends Transform {
    @Override
    public String getName() {
        return "auto_register";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_JARS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        HashSet<String> items = new HashSet<>();
        BaseTransform baseTransform = new BaseTransform(transformInvocation, new TransformCallBack() {

            @Override
            public byte[] process(String className, byte[] bytes, BaseTransform baseTransform) {
                String checkClassName = ClassUtils.path2Classname(className);
                if (checkClassName(checkClassName)) {
                    items.add(className);
                }
                return null;
            }

        });
        baseTransform.startTransform();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        File dest = outputProvider.getContentLocation("kronos_router", TransformManager.CONTENT_CLASS,
                ImmutableSet.of(QualifiedContent.Scope.PROJECT), Format.DIRECTORY);
        generateInitClass(dest.getAbsolutePath(), items);
    }

    static boolean checkClassName(String className) {
        if (className.contains("R\\$") || className.endsWith("R") || className.endsWith("BuildConfig")) {
            return false;
        }
        String packageList = "com.kronos.router.init";
        return className.contains(packageList);
    }

    public void generateInitClass(String directory, HashSet<String> items) {
        String className = Constant.REGISTER_CLASS_CONST.replace('.', '/');
        File dest = new File(directory, className + SdkConstants.DOT_CLASS);
        if (!dest.exists()) {
            try {
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, writer) {
                };
                cv.visit(50, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);
                MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
                        Constant.REGISTER_FUNCTION_NAME_CONST, "()V", null, null);
                mv.visitCode();
                for (String clazz : items) {
                    String input = clazz.replace(".class", "");
                    input = input.replace(".", "/");
                    Log.info("item:" + input);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, input, "init", "()V", false);
                }
                mv.visitMaxs(0, 0);
                mv.visitInsn(Opcodes.RETURN);
                mv.visitEnd();
                cv.visitEnd();
                dest.getParentFile().mkdirs();
                new FileOutputStream(dest).write(writer.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.kronos.autoregister;

import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.kronos.autoregister.helper.ClassFilterVisitor;
import com.kronos.autoregister.helper.TryCatchMethodVisitor;
import com.kronos.plugin.base.BaseTransform;
import com.kronos.plugin.base.ClassUtils;
import com.kronos.plugin.base.DeleteCallBack;
import com.kronos.plugin.base.TransformCallBack;

import org.apache.commons.io.IOUtils;
import org.gradle.internal.impldep.com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

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
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        HashSet<String> items = new HashSet<>();
        HashSet<String> deleteItems = new HashSet<>();
        BaseTransform baseTransform = new BaseTransform(transformInvocation, new TransformCallBack() {

            @Nullable
            @Override
            public byte[] process(@NotNull String className, @Nullable byte[] classBytes) {
                String checkClassName = ClassUtils.path2Classname(className);
                if (checkClassName(checkClassName)) {
                    items.add(className);
                }
                return new byte[0];
            }


        }, false);
        baseTransform.setDeleteCallBack(new DeleteCallBack() {
            @Override
            public void delete(String s, byte[] bytes) {
                String checkClassName = ClassUtils.path2Classname(s);
                if (checkClassName(checkClassName)) {
                    deleteItems.add(s);
                }
            }
        });
        baseTransform.openSimpleScan();
        baseTransform.startTransform();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        File dest = outputProvider.getContentLocation("kronos_router", TransformManager.CONTENT_CLASS,
                ImmutableSet.of(QualifiedContent.Scope.PROJECT), Format.DIRECTORY);
        generateInitClass(dest.getAbsolutePath(), items, deleteItems);
    }

    static boolean checkClassName(String className) {
        if (className.contains("R\\$") || className.endsWith("R") || className.endsWith("BuildConfig")) {
            return false;
        }
        String packageList = Constant.REGISTER_PACKAGE_CONST;
        return className.contains(packageList);
    }

    private void generateInitClass(String directory, HashSet<String> items, HashSet<String> deleteItems) {
        String className = Constant.REGISTER_CLASS_CONST.replace('.', '/');
        File dest = new File(directory, className + ".class");
        if (!dest.exists()) {
            try {
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                ClassVisitor cv = new ClassVisitor(Opcodes.ASM6, writer) {
                };
                cv.visit(50, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);
                TryCatchMethodVisitor mv = new TryCatchMethodVisitor(cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
                        Constant.REGISTER_FUNCTION_NAME_CONST, "()V", null, null), null, deleteItems);
                mv.visitCode();
                for (String clazz : items) {
                    String input = clazz.replace(".class", "");
                    input = input.replace(".", "/");
                    mv.addTryCatchMethodInsn(Opcodes.INVOKESTATIC, input, Constant.REGISTER_CLASS_FUNCTION_CONST, "()V", false);
                }
                mv.visitInsn(Opcodes.RETURN);
                mv.visitEnd();
                cv.visitEnd();
                dest.getParentFile().mkdirs();
                new FileOutputStream(dest).write(writer.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                modifyClass(dest, items, deleteItems);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void modifyClass(File file, HashSet<String> items, HashSet<String> deleteItems) throws IOException {
        try {
            InputStream inputStream = new FileInputStream(file);
            byte[] sourceClassBytes = IOUtils.toByteArray(inputStream);
            byte[] modifiedClassBytes = modifyClass(sourceClassBytes, items, deleteItems);
            if (modifiedClassBytes != null) {
                ClassUtils.saveFile(file, modifiedClassBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    byte[] modifyClass(byte[] srcClass, HashSet<String> items, HashSet<String> deleteItems) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor methodFilterCV = new ClassFilterVisitor(classWriter, items, deleteItems);
        ClassReader cr = new ClassReader(srcClass);
        cr.accept(methodFilterCV, ClassReader.SKIP_DEBUG);
        return classWriter.toByteArray();
    }

}

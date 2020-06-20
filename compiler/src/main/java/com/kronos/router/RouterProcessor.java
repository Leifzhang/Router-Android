package com.kronos.router;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.kronos.router.utils.Const;
import com.kronos.router.utils.Logger;
import com.kronos.router.utils.TypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
@SuppressWarnings("NullAway")
public class RouterProcessor extends AbstractProcessor {
    private Filer filer;
    private Logger logger;
    private String moduleName;
    private Elements elementUtils;
    private Types types;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Messager messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        logger = new Logger(messager);
        types = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        TypeUtils typeUtils = new TypeUtils(types, elementUtils);
        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            moduleName = options.get(Const.KEY_MODULE_NAME);
            if (StringUtils.isNotEmpty(moduleName)) {
                moduleName = moduleName.replaceAll("[^0-9a-zA-Z_]+", "");
            } else {
                moduleName = Const.DEFAULT_APP_MODULE;
            }
            logger.info("The user has configuration the module name, it was [" + moduleName + "]");
        }

    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<>();
        ret.add(BindRouter.class.getCanonicalName());
        return ret;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return ImmutableSet.of(Const.KEY_MODULE_NAME);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (CollectionUtils.isNotEmpty(annotations)) {
            initRouter(moduleName, roundEnv);
            return true;
        }
        return false;
    }

    private void initRouter(String name, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindRouter.class);
        if (elements.isEmpty()) {
            return;
        }
        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        TypeMirror type_Activity = elementUtils.getTypeElement(Const.ACTIVITY).asType();
        TypeMirror fragmentTm = elementUtils.getTypeElement(Const.FRAGMENT).asType();
        TypeMirror callBackTm = elementUtils.getTypeElement(Const.RUNNABLE).asType();
        //一、收集信息
        int count = 0;
        for (Element element : elements) {
            //检查element类型
            //field type
            BindRouter router = element.getAnnotation(BindRouter.class);
            ClassName className;
            if (element.getKind() == ElementKind.CLASS) {
                className = ClassName.get((TypeElement) element);
            } else if (element.getKind() == ElementKind.METHOD) {
                className = ClassName.get((TypeElement) element.getEnclosingElement());
            } else {
                throw new IllegalArgumentException("unknow type");
            }
            //class type
            String[] id = router.urls();
            for (String format : id) {
                int weight = router.weight();
                TypeMirror type = element.asType();
                if (types.isSubtype(type, callBackTm)) {
                    String callbackName = "callBack" + count;
                    initMethod.addStatement(className + " " + callbackName + "=new " + className + "()");
                    initMethod.addStatement("com.kronos.router.Router.map($S, " + callbackName + ")", format);
                    count++;
                    continue;
                }
                //   if (types.isSubtype(type_Activity, type)) {
                if (weight > 0) {
                    String bundleName = "bundle" + count;
                    initMethod.addStatement("android.os.Bundle " + bundleName + "=new android.os.Bundle();");
                    String optionsName = "options" + count;
                    initMethod.addStatement("com.kronos.router.model.RouterOptions " + optionsName + "=new com.kronos.router.model.RouterOptions("
                            + bundleName + ")");
                    initMethod.addStatement(optionsName + ".setWeight(" + weight + ")");
                    initMethod.addStatement("com.kronos.router.Router.map($S,$T.class," + optionsName + ")",
                            format, className);
                } else {
                    initMethod.addStatement("com.kronos.router.Router.map($S,$T.class)", format, className);
                }
            }
            count++;
            // }
        }
        String moduleName = "RouterInit_" + name;
        TypeSpec routerMapping = TypeSpec.classBuilder(moduleName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(initMethod.build())
                .build();
        try {
            JavaFile.builder("com.kronos.router.init", routerMapping)
                    .build()
                    .writeTo(filer);
        } catch (IOException ignored) {

        }
    }
}

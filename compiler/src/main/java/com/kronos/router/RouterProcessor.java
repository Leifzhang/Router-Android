package com.kronos.router;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<>();
        ret.add(BindRouter.class.getCanonicalName());
        return ret;
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindRouter.class);
       /* //一、收集信息
        for (Element element : elements) {
            //检查element类型
            //field type
            VariableElement variableElement = (VariableElement) element;
            //class type
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();//TypeElement
            String qualifiedName = typeElement.getQualifiedName().toString();
            BindRouter annotation = variableElement.getAnnotation(BindRouter.class);
            String[] id = annotation.value();
            for (String format : id) {
                initMethod.addStatement("com.kronos.router.Router.map($S,$T.class)", format, qualifiedName);
            }
        }*/
        String moduleName = "RouterInit";
        TypeSpec routerMapping = TypeSpec.classBuilder(moduleName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(initMethod.build())
                .build();
        try {
            JavaFile.builder("com.kronos.router.Router", routerMapping)
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}

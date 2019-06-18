package com.kronos.autoregister

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.kronos.autoregister.helper.InjectHelper
import com.kronos.autoregister.helper.Log
import groovy.io.FileType
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile

class AutoRegisterTransform extends Transform {
    Project project

    AutoRegisterTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "auto_register"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_JARS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        Log.info("transform")
        def inputs = transformInvocation.getInputs()
        def outputProvider = transformInvocation.outputProvider
        def context = transformInvocation.context
        def jarFile
        HashSet<String> items = new HashSet<>()
        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                addJarInitList(jarInput.file, items)
            }
            input.directoryInputs.each {
                DirectoryInput directoryInput ->
                    File dest = outputProvider.getContentLocation(directoryInput.name,
                            directoryInput.contentTypes, directoryInput.scopes,
                            Format.DIRECTORY)
                    File dir = directoryInput.file
                    if (dir) {
                        directoryInput.file.eachFileRecurse {
                            dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) {
                                File classFile ->
                                    String absolutePath = classFile.absolutePath.replace(dir.absolutePath + File.separator, "")
                                    String className = path2Classname(absolutePath)
                                    if (checkClassName(className)) {
                                        //key为相对路径
                                        items.add(absolutePath)
                                    }
                            }
                        }
                        FileUtils.copyDirectory(directoryInput.file, dest)
                    }
            }
        }
        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                String destName = jarInput.file.name
                /** 重名名输出文件,因为可能同名,会覆盖*/
                def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath).substring(0, 8);
                if (destName.endsWith(".jar")) {
                    destName = destName.substring(0, destName.length() - 4)
                }
                /** 获得输出文件*/
                File dest = outputProvider.getContentLocation(destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                def modifiedJar = jarInput.file
                if (isJarNeedModify(jarInput.file)) {
                    Log.info("isJarNeedModify:" + dest)
                    InjectHelper helper = new InjectHelper(jarInput.file, items)
                    modifiedJar = helper.modifyJarFile(context.temporaryDir)
                }
                FileUtils.copyFile(modifiedJar, dest)
            }
        }
    }


    static String path2Classname(String entryName) {
        return entryName.replace(File.separator, ".").replace(".class", "")
    }

    static boolean checkClassName(String className) {
        if (className.contains("R\$") || className.endsWith("R") || className.endsWith("BuildConfig")) {
            return false
        }
        String packageList = "com.kronos.router.init"
        return className.contains(packageList)
    }

    static boolean checkRouterInitClassName(String className) {
        String dexClassName = "com.kronos.router.RouterLoader"
        Log.info("className:" + className)
        return dexClassName == className
    }

    static boolean checkPackage(String className) {
        String packageName = "com.kronos.router"
        return className.contains(packageName)
    }


    /**
     * 该jar文件是否包含需要修改的类
     * @param jarFile
     * @return
     */
    static boolean addJarInitList(File jarFile, HashSet<String> items) {
        boolean modified = false
        if (jarFile) {
            /**
             * 读取原jar
             */
            def file = new JarFile(jarFile)
            Enumeration enumeration = file.entries()
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                String className
                if (entryName.endsWith(".class")) {
                    className = entryName.replace("/", ".").replace(".class", "")
                    if (!checkPackage(className)) {
                        return false
                    }
                    if (checkClassName(className)) {
                        items.add(entryName)
                        modified = true
                        break
                    }
                }
            }
            file.close()
        }
        return modified
    }

    /**
     * 该jar文件是否包含需要修改的类
     * @param jarFile
     * @return
     */
    static boolean isJarNeedModify(File jarFile) {
        boolean modified = false
        if (jarFile) {
            /**
             * 读取原jar
             */
            def file = new JarFile(jarFile)
            Enumeration enumeration = file.entries()
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                String className
                if (entryName.endsWith(".class")) {
                    className = entryName.replace("/", ".")
                            .replace(".class", "")
                    if (!checkPackage(className)) {
                        return false
                    }
                    if (checkRouterInitClassName(className)) {
                        modified = true
                        break
                    }
                }
            }
            file.close()
        }
        return modified
    }
}
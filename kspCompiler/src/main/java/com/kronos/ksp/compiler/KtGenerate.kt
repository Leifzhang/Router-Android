package com.kronos.ksp.compiler

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.*
import java.util.*

/**
 * @Author LiABao
 * @Since 2021/3/9
 */
class KtGenerate(private var logger: KSPLogger, name: String, private val codeGenerator: CodeGenerator) {

    private val initMethod: FunSpec.Builder = FunSpec.builder("register").apply {
        addAnnotation(AnnotationSpec.builder(JvmStatic::class).build())
    }

    private val className = "RouterInit${name.replace("[^0-9a-zA-Z_]+", "")}"
    private val specBuilder = FileSpec.builder("com.kronos.router.register", className)


    var index = 0

    fun addStatement(type: KSClassDeclaration, routerBindType: KSType) {

        val routerAnnotation = type.findAnnotationWithType(routerBindType) ?: return
        var isRunnable = false

        type.getAllSuperTypes().forEach {
            if (!isRunnable) {
                isRunnable = it.toClassName().canonicalName == RUNNABLE
            }
        }
        // logger.error("classType:${isRunnable}")
        val urls = routerAnnotation.getMember<ArrayList<String>>("urls")
        if (urls.isEmpty()) {
            return
        }
        val weight: Int = try {
            routerAnnotation.getMember("weight")
        } catch (e: Exception) {
            0
        }
        val interceptors = try {
            routerAnnotation.getMember<ArrayList<ClassName>>("interceptors")
        } catch (e: Exception) {
            null
        }

        urls.forEach { url ->
            if (isRunnable) {
                callBackStatement(url, type.toClassName(), weight, interceptors)
            } else {
                normalStatement(url, type.toClassName(), weight, interceptors)
            }
            index++
        }
    }

    private val optionClassName by lazy {
        MemberName("com.kronos.router.model", "RouterOptions")
    }
    private val routerMemberName by lazy {
        MemberName("com.kronos.router", "Router")
    }

    private fun callBackStatement(url: String, callBack: ClassName, weight: Int, interceptors: ArrayList<ClassName>?) {
        val memberName = "option$index"
        initMethod.addStatement("val $memberName =  %M()", optionClassName)
        buildInterceptors(memberName, interceptors)
        initMethod.addStatement("$memberName.weight=$weight")
        initMethod.addStatement("$memberName.callback=%T()", callBack)
        initMethod.addStatement("%M.map(url=%S,options= $memberName)", routerMemberName, url)
    }

    private fun normalStatement(url: String, activity: ClassName, weight: Int, interceptors: ArrayList<ClassName>?) {
        val memberName = "option$index"
        initMethod.addStatement("val $memberName =  %M()", optionClassName)
        buildInterceptors(memberName, interceptors)
        initMethod.addStatement("$memberName.weight=$weight")
        initMethod.addStatement("%M.map(url=%S,mClass=%T::class.java,options= $memberName)",
                routerMemberName, url, activity)
    }


    private fun buildInterceptors(memberName: String, interceptors: ArrayList<ClassName>?) {
        interceptors?.forEach {
            initMethod.addStatement("$memberName.addInterceptor(%T())", it)
        }
    }

    fun generateKt() {
        val helloWorld = TypeSpec.objectBuilder(className)
                .addFunction(initMethod.build())
                .build()
        specBuilder.addType(helloWorld)
        val spec = specBuilder.build()
        val file = codeGenerator.createNewFile(Dependencies.ALL_FILES, spec.packageName, spec.name)
        file.use {
            val content = spec.toString().toByteArray()
            it.write(content)
        }
    }


    companion object {
        const val RUNNABLE = "com.kronos.router.RouterCallback"
    }

}
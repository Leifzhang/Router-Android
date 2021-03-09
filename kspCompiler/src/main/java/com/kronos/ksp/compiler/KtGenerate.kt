package com.kronos.ksp.compiler

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import javax.lang.model.element.Modifier

/**
 * @Author LiABao
 * @Since 2021/3/9
 */
class KtGenerate(private var logger: KSPLogger) {

    val initMethod: FunSpec.Builder = FunSpec.builder("init")
            .addAnnotation(AnnotationSpec.builder(JvmStatic::class).build())
    var index = 0

    fun addStatement(type: KSClassDeclaration, routerBindType: KSType) {
        val routerAnnotation = type.findAnnotationWithType(routerBindType) ?: return
        var isRunnable = false

        type.getAllSuperTypes().forEach {
            if (!isRunnable) {
                isRunnable = it.toClassName().canonicalName == RUNNABLE
            }
        }
        logger.info("classType:${type.getAllSuperTypes()}")
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
            routerAnnotation.getMember<ArrayList<Class<Any>>>("interceptors")
        } catch (e: Exception) {
            null
        }
        urls.forEach {

        }
        index++
    }

    fun normalStatement(url: String, activity: ClassName, weight: Int, interceptors: ArrayList<Class<Any>>?) {
        initMethod.addStatement("com.kronos.router.Router.map(%P,%P::class,)", url, activity.canonicalName)
    }

    companion object {
        const val RUNNABLE = "com.kronos.router.RouterCallback"
    }

}
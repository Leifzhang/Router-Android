package com.kronos.ksp.compiler

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Origin
import com.kronos.router.BindRouter
import com.squareup.kotlinpoet.FileSpec
import java.io.IOException

@AutoService(SymbolProcessor::class)
class KspProcessor : SymbolProcessor {

    private lateinit var logger: KSPLogger
    private lateinit var codeGenerator: CodeGenerator
    private lateinit var routerBindType: KSType

    private lateinit var moduleName: String
    private val ktGenerate by lazy {
        KtGenerate(logger)
    }

    override fun init(options: Map<String, String>, kotlinVersion: KotlinVersion,
                      codeGenerator: CodeGenerator, logger: KSPLogger) {

        this.codeGenerator = codeGenerator
        this.logger = logger
        moduleName = options[Const.KEY_MODULE_NAME] ?: Const.DEFAULT_APP_MODULE
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val list = resolver.getSymbolsWithAnnotation(BindRouter::class.java.name)
        routerBindType = resolver.getClassDeclarationByName(
                resolver.getKSNameFromString(BindRouter::class.java.name)
        )?.asType() ?: kotlin.run {
            logger.error("JsonClass type not found on the classpath.")
            return emptyList()
        }
        list.asSequence().forEach {
            add(it)
        }
        val moduleName = "RouterInit_$moduleName"
        try {
            val builder = FileSpec.builder("com.kronos.router.init", moduleName)
                    .build()
            builder.writeTo(codeGenerator)
            //  builder.writeTo(codeGenerator.generatedFile)
        } catch (ignored: IOException) {
        }
        return emptyList()
    }

    private fun add(type: KSAnnotated) {
        logger.check(type is KSClassDeclaration && type.origin == Origin.KOTLIN, type) {
            "@JsonClass can't be applied to $type: must be a Kotlin class"
        }
        if (type !is KSClassDeclaration) return

        ktGenerate.addStatement(type, routerBindType)
        //class type
        //      val id: Array<String> = routerAnnotation.urls()
    }

    companion object {

    }
}
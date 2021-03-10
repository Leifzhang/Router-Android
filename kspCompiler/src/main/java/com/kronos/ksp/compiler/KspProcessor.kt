package com.kronos.ksp.compiler

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Origin
import com.kronos.router.BindRouter

@AutoService(SymbolProcessor::class)
class KspProcessor : SymbolProcessor {

    private lateinit var logger: KSPLogger
    private lateinit var codeGenerator: CodeGenerator
    private lateinit var routerBindType: KSType
    private var isload = false

    private lateinit var moduleName: String
    private val ktGenerate by lazy {
        KtGenerate(logger, moduleName, codeGenerator)
    }

    override fun init(options: Map<String, String>, kotlinVersion: KotlinVersion,
                      codeGenerator: CodeGenerator, logger: KSPLogger) {
        moduleName = options[Const.KEY_MODULE_NAME] ?: Const.DEFAULT_APP_MODULE
        this.codeGenerator = codeGenerator
        this.logger = logger
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isload) {
            return emptyList()
        }
        val symbols = resolver.getSymbolsWithAnnotation(BindRouter::class.java.name)
        routerBindType = resolver.getClassDeclarationByName(
                resolver.getKSNameFromString(BindRouter::class.java.name)
        )?.asType() ?: kotlin.run {
            logger.error("JsonClass type not found on the classpath.")
            return emptyList()
        }
        symbols.asSequence().forEach {
            add(it)
        }
        // logger.error("className:${moduleName}")
        try {
            ktGenerate.generateKt()
            isload = true
        } catch (e: Exception) {
            logger.error(
                    "Error preparing :" + " ${e.stackTrace.joinToString("\n")}"
            )
        }
        return symbols
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
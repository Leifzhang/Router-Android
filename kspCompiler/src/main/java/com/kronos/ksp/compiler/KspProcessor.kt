package com.kronos.ksp.compiler

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Origin
import com.kronos.ksp.compiler.Const.KEY_MODULE_NAME
import com.kronos.router.BindRouter

class KspProcessor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,

    private val moduleName: String
) : SymbolProcessor {

    private lateinit var routerBindType: KSType
    private var isload = false

    private val ktGenerate by lazy {
        KtGenerate(logger, moduleName, codeGenerator)
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
        return symbols.toList()
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


}

@AutoService(SymbolProcessorProvider::class)
class RouterProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        val moduleName = environment.options[KEY_MODULE_NAME] ?: Const.DEFAULT_APP_MODULE
        val codeGenerator = environment.codeGenerator
        val logger = environment.logger
        return KspProcessor(logger, codeGenerator, moduleName)
    }
}
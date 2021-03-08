package com.kronos.ksp.compiler

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.kronos.router.BindRouter

@AutoService(SymbolProcessor::class)
class KspProcessor : SymbolProcessor {

    override fun init(options: Map<String, String>, kotlinVersion: KotlinVersion,
                      codeGenerator: CodeGenerator, logger: KSPLogger) {

    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val list = resolver.getSymbolsWithAnnotation(BindRouter::class.java.name)
        list.asSequence().forEach {

        }
        //     val list = resolver.getSymbolsWithAnnotation(BindRouter)
        return emptyList()
    }
}
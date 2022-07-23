import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${getKotlinPluginVersion()}")
    implementation(project(":RouterAnnotation"))
    implementation("com.squareup:kotlinpoet:1.11.0")
    //一定要加这个  否则会出问题
    implementation("com.google.auto.service:auto-service-annotations:1.0.1")
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:${getKotlinPluginVersion()}")
    compileOnly("dev.zacsweers.autoservice:auto-service-ksp:1.0.0")
    compileOnly("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
    ksp("dev.zacsweers.autoservice:auto-service-ksp:1.0.0")
}
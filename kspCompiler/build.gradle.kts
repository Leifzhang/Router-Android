import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.4.30-1.0.0-alpha04"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${getKotlinPluginVersion()}")
    implementation(project(":RouterAnnotation"))
    implementation("com.squareup:kotlinpoet:1.7.2")
    //一定要加这个  否则会出问题
    implementation("com.google.auto.service:auto-service-annotations:1.0-rc7")
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:${getKotlinPluginVersion()}")
    compileOnly("dev.zacsweers.autoservice:auto-service-ksp:0.3.2")
    compileOnly("com.google.devtools.ksp:symbol-processing-api:1.4.30-1.0.0-alpha04")
    ksp("dev.zacsweers.autoservice:auto-service-ksp:0.3.2")
}
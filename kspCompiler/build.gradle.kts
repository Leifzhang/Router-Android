import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    kotlin("jvm")
    //id("com.google.devtools.ksp") version "1.4.30-1.0.0-alpha04"
    id("java-library")
    id("kotlin")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${getKotlinPluginVersion()}")
    implementation(project(":RouterAnnotation"))
    implementation("com.google.auto.service:auto-service-annotations:1.0-rc7")
    implementation("dev.zacsweers.autoservice:auto-service-ksp:0.3.2")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.4.30-1.0.0-alpha04")
}
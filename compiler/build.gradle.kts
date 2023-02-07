import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")
}

java.sourceSets["main"].java {
    srcDir("src/link/main")
}


dependencies {
    implementation(libs.auto.service)
    implementation(libs.javapoet)
    implementation(libs.commons.lang3)
    implementation(libs.commons.collections4)
        // implementation(libs.annotation)
    implementation(files(org.gradle.internal.jvm.Jvm.current().toolsJar))
    implementation(libs.kotlin.stdlib.jdk7)
    kapt(libs.auto.service)
}
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk=libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
    }
}

dependencies {
    compileOnly(libs.core.ktx)
    compileOnly(project(":EmptyLoader"))
    implementation(libs.annotation)
    implementation(libs.kotlin.stdlib.jdk7)
}
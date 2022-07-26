import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(32)

    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(32)
    }
}

dependencies {
    compileOnly("androidx.appcompat:appcompat:1.3.0-alpha02")
    compileOnly(project(":EmptyLoader"))
    implementation(project(":RouterAnnotation"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${getKotlinPluginVersion()}")
}
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.github.dcendents.android-maven")
    id("com.jfrog.bintray")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(30)
    }

    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    compileOnly("androidx.appcompat:appcompat:1.3.0-alpha02")
    compileOnly(project(":EmptyLoader"))
    implementation(project(":RouterAnnotation"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${getKotlinPluginVersion()}")
}
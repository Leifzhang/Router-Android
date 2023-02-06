import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    // 这个 id 就是在 versionPlugin 文件夹下 build.gradle.kts.kts.kts.kts 文件内定义的id
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("router-register")
}
apply {
    plugin("kotlin-android")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        //  applicationId("com.kronos.router")
        minSdk = libs.versions.minSdk.get().toInt()
        multiDexEnabled = true

    }
    buildTypes {
        getByName("release") {
            // minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    productFlavors {
        create("flavor1") {
            dimension = "type"
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"
        }
        create("flavor2") {
            dimension = "type"
        }
    }
    buildFeatures {
        viewBinding = true
    }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.13.2")
    implementation(libs.core.ktx)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.recyclerview)
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.retrofit)
    implementation(libs.fastjson)
    // 如果你不要用transform
    val routerVersion = "0.5.1"
    implementation(libs.annotation)
    implementation(libs.routerLib)

    implementation(project(":secondmoudle"))
    implementation(libs.routerCoroutine)
    // kapt("libs.compiler:$routerVersion")

    // ViewModel
    implementation(libs.bundles.lifecycles)
    kapt(libs.lifecycle.compiler)
    ksp(project(":kspCompiler"))
}

import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    // 这个 id 就是在 versionPlugin 文件夹下 build.gradle.kts.kts 文件内定义的id
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version "1.4.30-1.0.0-alpha04"
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")

    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(29)
        versionCode(1)
        versionName("1.0")
        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")

    }


    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}


dependencies {
    implementation("androidx.appcompat:appcompat:1.3.0")
    testImplementation("junit:junit:4.13.2")
    val routerVersion = "0.5.1"
    implementation("com.github.leifzhang:RouterAnnotation:$routerVersion")
    implementation("com.github.leifzhang:RouterLib:$routerVersion")
    implementation("com.github.leifzhang:CoroutineSupport:$routerVersion")
    ksp(project(":kspCompiler"))
    //kapt(project(":compiler"))
    //implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${getKotlinPluginVersion()}")
}
ksp {
    arg("ROUTER_MODULE_NAME", project.name)
}
kapt {
    arguments {
        arg("ROUTER_MODULE_NAME", project.name)
    }
}

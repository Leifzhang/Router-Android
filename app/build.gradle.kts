import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    // 这个 id 就是在 versionPlugin 文件夹下 build.gradle.kts.kts.kts.kts 文件内定义的id
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version "1.4.30-1.0.0-alpha04"
    id("router-register")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId("com.kronos.router")
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode(1)
        versionName("1.0")
        multiDexEnabled = true
    }
    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        targetCompatibility(1.8)
        sourceCompatibility(1.8)
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":secondmoudle"))
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.appcompat:appcompat:1.3.0-alpha02")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${getKotlinPluginVersion()}")
    implementation("androidx.recyclerview:recyclerview:1.2.0-beta01")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.0")
    // 如果你不要用transform
    implementation(project(":RouterAnnotation"))
    implementation(project(":RouterLib"))
    implementation(project(":CoroutineSupport"))
    kapt(project(":compiler"))
    //  ksp(project(":kspCompiler"))
}

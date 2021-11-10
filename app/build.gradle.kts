import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    // 这个 id 就是在 versionPlugin 文件夹下 build.gradle.kts.kts.kts.kts 文件内定义的id
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version "1.4.30-1.0.0-alpha04"
    //   id("router-register")
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

    buildFeatures {
        viewBinding = true
    }

}
println(project.android.javaClass.canonicalName)
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${getKotlinPluginVersion()}")
    implementation("androidx.recyclerview:recyclerview:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.activity:activity-ktx:1.2.3")
    implementation("androidx.fragment:fragment-ktx:1.3.3")
    implementation("com.alibaba:fastjson:1.1.72.android")
    implementation("androidx.core:core-ktx:1.5.0")
    // 如果你不要用transform
    val routerVersion = "0.5.1"
    implementation("com.github.leifzhang:RouterAnnotation:$routerVersion")
    implementation("com.github.leifzhang:RouterLib:$routerVersion")

    implementation("com.github.leifzhang:secondmoudle:$routerVersion")
    implementation("com.github.leifzhang:CoroutineSupport:$routerVersion")
    kapt("com.github.leifzhang:compiler:$routerVersion")

    val lifecycle_version = "2.3.1"

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Lifecycles only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")

    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")

    // Annotation processor
    //   kapt("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
    // alternately - if using Java8, use the following instead of lifecycle-compiler
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")

    // optional - helpers for implementing LifecycleOwner in a Service
    implementation("androidx.lifecycle:lifecycle-service:$lifecycle_version")

    // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")

    // optional - ReactiveStreams support for LiveData
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycle_version")

    //  ksp(project(":kspCompiler"))
}

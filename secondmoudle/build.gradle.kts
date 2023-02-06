import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    // 这个 id 就是在 versionPlugin 文件夹下 build.gradle.kts.kts 文件内定义的id
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
    }


    buildFeatures {
        viewBinding = true
    }
    lintOptions {
        error("UnusedImports")
    }

}

dependencies {
    implementation(libs.appcompat)
    testImplementation("junit:junit:4.13.2")
    implementation(libs.annotation)
    implementation(libs.routerLib)
    implementation(libs.routerCoroutine)
    //  ksp(project(":kspCompiler"))
    kapt(libs.compiler)
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

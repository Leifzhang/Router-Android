import java.util.concurrent.TimeUnit
import org.gradle.*

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/central/") }
        maven { setUrl("https://dl.bintray.com/leifzhang/maven") }
        jcenter()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath(kotlin("gradle-plugin", version = "1.4.30"))
        // NOTE: Do not place your application dependencies here;elong
        // in the individual module build.gradle files
    }
}

plugins {
    // 这个 id 就是在 versionPlugin 文件夹下 build.gradle 文件内定义的id
    kotlin("jvm") version "1.4.30" apply false
    id("router-register") apply false
}

allprojects {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/central/") }
        maven { setUrl("https://dl.bintray.com/leifzhang/maven") }
        jcenter()
        google()
    }
    configurations.all {
        resolutionStrategy.dependencySubstitution.all {
            if (requested is ModuleComponentSelector) {
                val moduleRequested = requested as ModuleComponentSelector
                val p = rootProject.allprojects.find { p ->
                    (p.group == moduleRequested.group && p.name == moduleRequested.module)
                }
                if (p != null) {
                    useTarget(project(p.path), "selected local project")
                }

            }
        }
    }
    afterEvaluate {
        allprojects.forEach {
            val depFile = "$rootDir/dependenciesKt.gradle.kts"
            val bintray = "$rootDir/upload_bintray.gradle"
            it.apply(from = depFile)
            it.apply(from = bintray)


        }
    }

}
subprojects {
    val pluginManager = pluginManager
    val toolChainVersion = project.findProperty("moshix.javaLanguageVersion")?.toString() ?: "8"
    //if (pluginManager.hasPlugin("java")) {
    pluginManager.withPlugin("java-library") {
        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8

        }
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
}

import java.util.concurrent.TimeUnit
import org.gradle.*
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include

// Top-level build file where you can add configuration options common to all sub-projects/modules.


plugins {
    // 这个 id 就是在 versionPlugin 文件夹下 build.gradle 文件内定义的id
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply (false)
    id("com.google.devtools.ksp") version ("1.7.10-1.0.6") apply (false)
    id("router-register") apply false
}

allprojects {
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


}
subprojects {
    val pluginManager = pluginManager
    //if (pluginManager.hasPlugin("java")) {
    pluginManager.withPlugin("java-library") {
        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8

        }
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
            //  @Suppress("SuspiciousCollectionReassignment")
            //  freeCompilerArgs += listOf("-Xjsr305=strict", "-progressive")
        }
    }
    pluginManager.withPlugin("com.google.devtools.ksp") {
        this.name
    }
    group = rootProject.properties["PROJ_GROUP"] ?: ""
    version = rootProject.properties["PROJ_VERSION"] ?: ""
    afterEvaluate { }
}
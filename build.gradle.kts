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
        classpath("com.android.tools.build:gradle:4.2.0")
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
            it.apply(from = depFile)
        }
    }

}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
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
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
            kotlinOptions {
                //   jvmTarget = "1.8"
                //  @Suppress("SuspiciousCollectionReassignment")
                //  freeCompilerArgs += listOf("-Xjsr305=strict", "-progressive")
            }
        }
    }
    pluginManager.withPlugin("com.google.devtools.ksp") {
        this.name
    }
    group = rootProject.properties["PROJ_GROUP"] ?: ""
    version = rootProject.properties["PROJ_VERSION"] ?: ""
}

// 耗时统计kt化
class TimingsListener : TaskExecutionListener, BuildListener {
    private var startTime: Long = 0L
    private var timings = linkedMapOf<String, Long>()


    override fun beforeExecute(task: Task) {
        startTime = System.nanoTime()
    }

    override fun afterExecute(task: Task, state: TaskState) {
        val ms = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
        task.path
        timings[task.path] = ms
        project.logger.warn("${task.path} took ${ms}ms")
    }

    override fun buildFinished(result: BuildResult) {
        project.logger.warn("Task timings:")
        timings.forEach {
            if (it.value >= 50) {
                project.logger.warn("${it.key} cos  ms  ${it.value}\n")
            }
        }
    }

    override fun buildStarted(gradle: Gradle) {

    }

    override fun settingsEvaluated(settings: Settings) {
    }

    override fun projectsLoaded(gradle: Gradle) {

    }

    override fun projectsEvaluated(gradle: Gradle) {

    }

}

gradle.addListener(TimingsListener())
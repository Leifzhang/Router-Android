import java.util.concurrent.TimeUnit
import org.gradle.*
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include

// Top-level build file where you can add configuration options common to all sub-projects/modules.


plugins {
    // 这个 id 就是在 versionPlugin 文件夹下 build.gradle 文件内定义的id
    id("com.android.application") version ("7.1.1") apply (false)
    id("com.android.library") version ("7.1.1") apply (false)
    id("org.jetbrains.kotlin.android") version ("1.7.10") apply (false)
    id("org.jetbrains.kotlin.jvm") version ("1.7.10") apply (false)
    id("com.google.devtools.ksp") version ("1.7.10-1.0.6") apply (false)
    id("router-register") apply false
}
ext {
    val routerVersion = rootProject.properties["PROJ_VERSION"] ?: ""
    set("routerVersion", routerVersion)
}
allprojects {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/central/") }
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
}
/*

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
*/

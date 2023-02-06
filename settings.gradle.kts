pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}
dependencyResolutionManagement {
    //  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/central/") }
        google()
        mavenCentral()
    }
}
includeBuild("./Plugin")
include(":CoroutineSupport")
include(":app")
include(":RouterLib")
include(":RouterAnnotation")
include(":compiler")
include(":secondmoudle")
include(":EmptyLoader")
include(":kspCompiler")
enableFeaturePreview("VERSION_CATALOGS")
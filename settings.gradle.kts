pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
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
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    id("java-library")
    id("kotlin")
}
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${getKotlinPluginVersion()}")
}

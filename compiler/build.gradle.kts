import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")
}

dependencies {
    implementation("com.google.auto.service:auto-service:1.0-rc7")
    implementation("com.squareup:javapoet:1.13.0")
    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("org.apache.commons:commons-collections4:4.1")
    implementation(project(":RouterAnnotation"))
    implementation(files(org.gradle.internal.jvm.Jvm.current().toolsJar))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${getKotlinPluginVersion()}")
    kapt("com.google.auto.service:auto-service:1.0-rc7")
}
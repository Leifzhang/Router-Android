plugins {
    //  id("com.google.devtools.ksp") version kspVersion
    id("java-library")
    id("kotlin")
}
task("java") {

}

/*
java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}
*/

dependencies {
    //implementation ("org.jetbrains.kotlin:kotlin-stdlib:$kotlinver")
    implementation("com.google.auto.service:auto-service-annotations:1.0-rc7")
    implementation("dev.zacsweers.autoservice:auto-service-ksp:0.3.2")
}
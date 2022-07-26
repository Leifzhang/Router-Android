import java.util.*

plugins {
    id("java-library")
    id("kotlin")
}


task("stubLib", JavaCompile::class) {
    source(file("src/stub/java"))
    classpath = project.files(getAndroidJar("32"))
    // libraries
    destinationDirectory.set(File(project.buildDir, "/tmp/stubLibs"))
}

task("stubLibsJar", Jar::class) {
    archiveBaseName.set("stub")
    archiveVersion.set("1.0")
    from(tasks.getByName("stubLib"))
    include("**/*.class")
}

dependencies {
    //  implementation fileTree(dir: 'libs', include: ['*.jar'])
    //    compileOnly(getAndroidJar("32"))
    val stub = tasks.getByName("stubLibsJar").outputs.files
    compileOnly(stub)
}


fun getAndroidJar(compileSdkVersion: String): String {
    var androidSdkDir =
        System.getenv(com.android.tools.analytics.Environment.EnvironmentVariable.ANDROID_SDK_HOME.key)
    if (androidSdkDir.isNullOrEmpty()) {
        val propertiesFile = rootProject.file("local.properties")
        if (propertiesFile.exists()) {
            val properties = Properties()
            properties.load(propertiesFile.inputStream())
            androidSdkDir = properties.getProperty("sdk.dir")
        }
    }
    if (androidSdkDir.isNullOrEmpty()) {
        throw  StopExecutionException("please declares your 'sdk.dir' to file 'local.properties'")
    }
    val path = "platforms${File.separator}android-${compileSdkVersion}${File.separator}android.jar"
    return File(androidSdkDir.toString(), path).absolutePath
}


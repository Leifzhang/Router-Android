import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.github.dcendents.android-maven")
    id("com.jfrog.bintray")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(30)
    }

    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    compileOnly("androidx.appcompat:appcompat:1.3.0-alpha02")
    compileOnly(project(":EmptyLoader"))
    implementation(project(":RouterAnnotation"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${getKotlinPluginVersion()}")
}
/*
def siteUrl = 'https://github.com/Leifzhang/AndroidRouter' // 项目的主页
def gitUrl = 'https://github.com/Leifzhang/AndroidRouter.git' // Git仓库的url
group = PROJ_GROUP // Maven Group ID for the artifact，一般填你唯一的包名
install {
    repositories.mavenInstaller {
        // This generates POM.xml with proper parameters
        pom {
            project {
                packaging 'aar'
                // Add your description here
                name 'An Android Router Lib' //项目描述
                url siteUrl
                        // Set your license
                        licenses {
                            license {
                                name 'The Apache Software License, Version 2.0'
                                url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
                            }
                        }
                developers {
                    developer {
                        id 'LeifZhang'    //填写的一些基本信息
                        name '肉老师'
                        email 'leifzhanggithub@gmail.com'
                    }
                }
                scm {
                    connection gitUrl
                            developerConnection gitUrl
                            url siteUrl
                }
            }
        }
    }
}
task sourcesJar (type: Jar) {
    from android . sourceSets . main . java . srcDirs
            classifier = 'sources'
}
artifacts {
    archives sourcesJar
}
Properties properties = new Properties()
properties.load(project.rootProject.file('local.properties').newDataInputStream())
bintray {
    user = properties.getProperty("bintray.user")
    key = properties.getProperty("bintray.apikey")
    configurations = ['archives']
    pkg {
        repo = "maven"
        name = "RouterLib"    //发布到JCenter上的项目名字
        websiteUrl = siteUrl
        vcsUrl = gitUrl
        licenses = ["Apache-2.0"]
        publish = true
    }
}
repositories {
    mavenCentral()
}*/

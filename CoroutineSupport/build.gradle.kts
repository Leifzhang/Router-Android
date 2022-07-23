import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(32)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(32)
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = 1.8.toString()
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${getKotlinPluginVersion()}")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.0")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    implementation(project(":RouterAnnotation"))
    implementation(project(":RouterLib"))
}
//version = PROJ_VERSION
//group = PROJ_GROUP // Maven Group ID for the artifact，一般填你唯一的包名
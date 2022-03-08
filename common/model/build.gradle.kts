plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version Versions.kotlin
}

android {
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}

kotlin {
    android()
    jvm("desktop")

    sourceSets["commonMain"].dependencies {
        implementation(Dependencies.Kotlin.serializationCore)
    }

    sourceSets["androidMain"].dependencies {

    }

    sourceSets["desktopMain"].dependencies {
        implementation(Dependencies.Log.slf4j)
    }
}

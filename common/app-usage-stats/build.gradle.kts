plugins {
    kotlin("multiplatform")
    id("com.android.library")
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
        implementation(project(":common:model"))

        with(Dependencies.Koin) {
            api(core)
        }
        with(Dependencies.Log) {
            api(kermit)
        }
    }

    sourceSets["commonTest"].dependencies {
        implementation(Dependencies.Mockk.core)
        implementation(Dependencies.Mockk.commonMultiplatform)
        implementation(Dependencies.Kotlin.dateTime)
        implementation(Dependencies.Koin.test)
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
    }

    sourceSets["androidMain"].dependencies {
        with(Dependencies.Koin) {
            api(android)
        }
    }

    sourceSets["androidTest"].dependencies {
        //implementation(Dependencies.Android.JUnit.core)
    }

    sourceSets["desktopMain"].dependencies {
        //implementation(Dependencies.Log.slf4j)
    }

    sourceSets["desktopTest"].dependencies {
    }
}

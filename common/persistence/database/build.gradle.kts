plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("ReluctDatabase") {
        packageName = "work.racka.reluct.common.database.db"
        sourceFolders = listOf("sqldelight")
    }
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

        with(Dependencies.Squareup.SQLDelight) {
            implementation(runtime)
            implementation(coroutineExtensions)
        }

        with(Dependencies.Koin) {
            api(core)
            api(test)
        }

        with(Dependencies.Log) {
            api(kermit)
        }
    }

    sourceSets["androidMain"].dependencies {
        implementation(Dependencies.Squareup.SQLDelight.androidDriver)
    }

    sourceSets["desktopMain"].dependencies {
        implementation(Dependencies.Squareup.SQLDelight.sqliteDriver)
        implementation(Dependencies.Log.slf4j)
    }
}

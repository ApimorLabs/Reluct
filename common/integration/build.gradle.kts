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
        implementation(project(":common:model"))
        implementation(project(":common:persistence:database"))
        implementation(project(":common:persistence:settings"))
        implementation(project(":common:features:tasks"))
        implementation(project(":common:features:goals"))

        implementation(Dependencies.Kotlin.serializationCore)

        implementation(Dependencies.OrbitMVI.core)

        with(Dependencies.Squareup.SQLDelight) {
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

        implementation(Dependencies.Revenuecat.android)

        implementation(Dependencies.Android.Essential.lifecycleRuntimeKtx)
        implementation(Dependencies.Android.Compose.viewModel)

        with(Dependencies.Koin) {
            api(android)
        }
    }

    sourceSets["desktopMain"].dependencies {
        implementation(Dependencies.Log.slf4j)
    }
}

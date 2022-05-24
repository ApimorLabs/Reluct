import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
        implementation(project(":common:data"))
        implementation(project(":common:model"))
        implementation(project(":common:persistence:database"))

        implementation(Dependencies.OrbitMVI.core)

        implementation(Dependencies.Kotlin.Coroutines.core)

        with(Dependencies.Koin) {
            api(core)
            api(test)
        }

        with(Dependencies.Log) {
            api(kermit)
        }
    }

    sourceSets["commonTest"].dependencies {
        implementation(Dependencies.Mockk.core)
        implementation(Dependencies.Mockk.commonMultiplatform)

        implementation(Dependencies.OrbitMVI.test)

        implementation(Dependencies.Kotlin.dateTime)
        implementation(Dependencies.Koin.test)
        implementation(Dependencies.Kotlin.Coroutines.test)
        implementation(Dependencies.Squareup.Testing.turbine)
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
    }

    sourceSets["androidMain"].dependencies {
        with(Dependencies.Koin) {
            api(android)
        }
        implementation(Dependencies.Android.Essential.lifecycleRuntimeKtx)
        implementation(Dependencies.Android.Compose.viewModel)

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

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
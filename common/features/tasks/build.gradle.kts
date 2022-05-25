import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:data"))
                implementation(project(":common:model"))
                implementation(project(":common:persistence:database"))

                implementation(Dependencies.Kotlin.Coroutines.core)
                implementation(Dependencies.Koin.core)
                implementation(Dependencies.Log.kermit)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(Dependencies.Mockk.core)
                implementation(Dependencies.Mockk.commonMultiplatform)
                implementation(Dependencies.Kotlin.dateTime)
                implementation(Dependencies.Koin.test)
                implementation(Dependencies.Kotlin.Coroutines.test)
                implementation(Dependencies.Squareup.Testing.turbine)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Koin.android)
                implementation(Dependencies.Android.Essential.lifecycleRuntimeKtx)
                implementation(Dependencies.Android.Compose.viewModel)
            }
        }

        val androidTest by getting

        val desktopMain by getting {
            dependencies {

            }
        }

        val desktopTest by getting
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
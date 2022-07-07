plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose") version Versions.composeDesktop
}

android {
    namespace = "work.racka.reluct.common.mvvm"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Kotlin.Coroutines.core)
                implementation(Dependencies.Koin.core)
            }
        }

        val commonTest by getting {
            dependencies {
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
                implementation(Dependencies.Android.Essential.coreViewModel)
                implementation(Dependencies.Android.Compose.viewModel)
            }
        }

        val androidTest by getting

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(Dependencies.ArkIvanov.Decompose.decompose)
            }
        }

        val desktopTest by getting
    }
}

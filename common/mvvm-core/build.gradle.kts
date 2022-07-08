plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose") version Versions.composeDesktop
}

android {
    namespace = "work.racka.common.mvvm"

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
    js(BOTH) {
        useCommonJs()
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Koin.core)
                implementation(Dependencies.Kotlin.Coroutines.core)
                implementation(Dependencies.ArkIvanov.Decompose.decompose)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(Dependencies.Koin.test)
                implementation(Dependencies.Kotlin.Coroutines.test)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Koin.android)
                api(Dependencies.Android.Essential.coreViewModel)
                api(Dependencies.Android.Compose.viewModel)
            }
        }

        val androidTest by getting

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
            }
        }

        val desktopTest by getting

        val jsMain by getting {
            dependencies {

            }
        }
    }
}

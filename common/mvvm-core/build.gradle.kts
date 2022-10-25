plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

android {
    namespace = "work.racka.common.mvvm"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
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
                implementation(libs.koin.core)
                implementation(libs.coroutines.core)
                implementation(libs.decompose.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.koin.test)
                implementation(libs.coroutines.test)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                // Defined as API deps - Don't add again in modules that use this module
                api(libs.koin.android)
                api(libs.viewmodel.core)
                api(libs.viewmodel.compose)
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

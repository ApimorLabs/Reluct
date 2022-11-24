plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    namespace = "work.racka.reluct.common.core_navigation"

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

    sourceSets {
        val commonMain by getting {
            dependencies {

            }
        }

        val commonTest by getting

        val androidMain by getting {
            dependencies {
                // Compose
                implementation(libs.compose.material.icons.core)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.navigation.compose)
            }
        }

        val androidTest by getting

        val desktopMain by getting {
            dependencies {
                implementation(libs.decompose.core)
            }
        }

        val desktopTest by getting

    }
}

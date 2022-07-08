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
        kotlinCompilerExtensionVersion = Versions.composeCompiler
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
                with(Dependencies.Android.Compose) {
                    implementation(materialIconsCore)
                    implementation(materialIconsExtended)
                }
                implementation(Dependencies.Android.Compose.navigation)
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

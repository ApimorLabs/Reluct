plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

android {
    namespace = "work.racka.reluct.common.features.screen_time"
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get() }
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:core-navigation"))
                implementation(project(":common:domain"))
                implementation(project(":common:model"))
                implementation(project(":common:mvvm-core"))
                implementation(project(":common:persistence:settings"))
                implementation(project(":common:system-services"))

                implementation(libs.coroutines.core)
                implementation(libs.koin.core)
                implementation(libs.kermit.log)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.koin.test)
                implementation(libs.coroutines.test)
                implementation(libs.turbine.test)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.core.ktx)
                implementation(libs.savedState)

                // Compose
                implementation(project(":android:compose:theme"))
                implementation(project(":android:compose:components"))
                implementation(libs.bundles.compose.core)
                implementation(libs.viewmodel.compose)
            }
        }

        val androidTest by getting

        val desktopMain by getting {
            dependencies {
                implementation(libs.slf4j.simple)
            }
        }

        val desktopTest by getting
    }
}

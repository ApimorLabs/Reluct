plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    namespace = "work.racka.reluct.common.features.onboarding"
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:domain"))
                implementation(project(":common:model"))
                implementation(project(":common:mvvm-core"))
                implementation(project(":common:persistence:settings"))

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
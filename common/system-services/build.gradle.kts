plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

android {
    namespace = "work.racka.reluct.common.services"
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:model"))
                implementation(libs.koin.core)
                implementation(libs.kermit.log)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.date.time)
                implementation(libs.coroutines.core)
            }
        }

        val commonTest by getting

        val androidMain by getting {
            dependencies {
                implementation(libs.koin.android)
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

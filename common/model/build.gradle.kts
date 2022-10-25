plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

android {
    namespace = "work.racka.reluct.common.model"
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.core)
                api(libs.kotlinx.date.time)
            }
        }

        val commonTest by getting

        val androidMain by getting {
            dependencies {

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

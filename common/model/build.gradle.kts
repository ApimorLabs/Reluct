plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version Versions.kotlin
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
                with(Dependencies.Kotlin) {
                    implementation(serializationCore)
                    implementation(dateTime)
                }
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
                implementation(Dependencies.Log.slf4j)
            }
        }

        val desktopTest by getting
    }
}

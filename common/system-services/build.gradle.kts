plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version Versions.kotlin
}

android {
    namespace = "work.racka.reluct.common.system_services"
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:model"))
                implementation(Dependencies.Koin.core)
                implementation(Dependencies.Log.kermit)
                with(Dependencies.Kotlin) {
                    implementation(serializationCore)
                    implementation(dateTime)
                    implementation(Dependencies.Kotlin.Coroutines.core)
                }
            }
        }

        val commonTest by getting

        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Koin.android)
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

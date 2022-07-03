plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    namespace = "work.racka.reluct.common.features.goals"
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

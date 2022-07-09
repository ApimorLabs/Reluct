plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    namespace = "work.racka.reluct.common.data"
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:app-usage-stats"))
                implementation(project(":common:model"))
                implementation(project(":common:persistence:database"))
                implementation(project(":common:persistence:settings"))

                implementation(Dependencies.Kotlin.Coroutines.core)
                implementation(Dependencies.Koin.core)
                implementation(Dependencies.Log.kermit)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(Dependencies.Mockk.core)
                implementation(Dependencies.Mockk.commonMultiplatform)
                implementation(Dependencies.Kotlin.dateTime)
                implementation(Dependencies.Koin.test)
                implementation(Dependencies.Kotlin.Coroutines.test)
                implementation(Dependencies.Squareup.Testing.turbine)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Koin.android)
            }
        }

        val androidTest by getting

        val desktopMain by getting

        val desktopTest by getting
    }
}

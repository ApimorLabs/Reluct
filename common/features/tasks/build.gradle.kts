plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    namespace = "work.racka.reluct.common.features.tasks"
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:core-navigation"))
                implementation(project(":common:data"))
                implementation(project(":common:model"))
                implementation(project(":common:mvvm-core"))

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
                implementation(Dependencies.Android.Essential.coreKtx)
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
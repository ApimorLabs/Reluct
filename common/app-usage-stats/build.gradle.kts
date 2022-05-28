plugins {
    kotlin("multiplatform")
    id("com.android.library")
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
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(Dependencies.Mockk.core)
                implementation(Dependencies.Mockk.commonMultiplatform)
                implementation(Dependencies.Kotlin.dateTime)
                implementation(Dependencies.Koin.test)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Log.timber)
                implementation(Dependencies.Koin.android)
            }
        }

        val androidTest by getting

        val desktopMain by getting

        val desktopTest by getting
    }
}

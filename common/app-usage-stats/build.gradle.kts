plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    namespace = "work.racka.reluct.common.app.usage.stats"
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
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlinx.date.time)
                implementation(libs.koin.test)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.palette)
                implementation(libs.timber.log)
                implementation(libs.koin.android)
            }
        }

        val androidTest by getting

        val desktopMain by getting

        val desktopTest by getting
    }
}

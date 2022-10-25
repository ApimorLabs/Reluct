plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    namespace = "work.racka.reluct.common.domain"
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:app-usage-stats"))
                api(project(":common:billing")) // We have important classes to expose
                implementation(project(":common:core-navigation"))
                implementation(project(":common:model"))
                implementation(project(":common:persistence:database"))
                implementation(project(":common:persistence:settings"))
                implementation(project(":common:system-services"))

                implementation(libs.coroutines.core)
                implementation(libs.koin.core)
                implementation(libs.kermit.log)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlinx.date.time)
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
                implementation(libs.koin.android)
            }
        }

        val androidTest by getting

        val desktopMain by getting

        val desktopTest by getting
    }
}

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    namespace = "work.racka.reluct.common.authentication"
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:model"))
                implementation(project(":common:persistence:settings"))

                implementation(Dependencies.Kotlin.Coroutines.core)
                implementation(Dependencies.Koin.core)
                implementation(Dependencies.Log.kermit)
            }
        }

        val commonTest by getting {
            dependencies {
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
                implementation(Dependencies.Android.Essential.coreKtx)
                implementation(Dependencies.Kotlin.Coroutines.playServices)
                with(Dependencies.Android.Firebase) {
                    implementation(project.dependencies.platform(bom))
                    implementation(auth)
                    implementation(analytics)
                }
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

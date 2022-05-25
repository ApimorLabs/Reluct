import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Kotlin.Coroutines.core)
                implementation(Dependencies.Koin.core)
                implementation(Dependencies.Log.kermit)
                with(Dependencies.RusshWolf.MultiplatformSettings) {
                    implementation(core)
                    implementation(noArg)
                }
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(Dependencies.RusshWolf.MultiplatformSettings.test)
                implementation(Dependencies.Koin.test)
                implementation(Dependencies.Kotlin.Coroutines.test)
                implementation(Dependencies.Squareup.Testing.turbine)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

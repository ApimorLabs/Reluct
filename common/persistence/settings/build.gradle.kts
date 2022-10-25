import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

android {
    namespace = "work.racka.reluct.common.settings"
}

kotlin {
    jvm("desktop")
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.date.time)
                implementation(libs.coroutines.core)
                implementation(libs.koin.core)
                implementation(libs.kermit.log)
                implementation(libs.multiplatform.settings.core)
                implementation(libs.multiplatform.settings.noArg)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.multiplatform.settings.test)
                implementation(libs.koin.test)
                implementation(libs.coroutines.test)
                implementation(libs.turbine.test)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

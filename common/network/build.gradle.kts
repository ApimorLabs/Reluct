import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

android {
    namespace = "work.racka.reluct.common.network"
}

kotlin {
    jvm("desktop")
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:authentication"))
                implementation(project(":common:persistence:database"))
                implementation(project(":common:persistence:settings"))
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.date.time)
                implementation(libs.coroutines.core)
                implementation(libs.koin.core)
                implementation(libs.kermit.log)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.koin.test)
                implementation(libs.coroutines.test)
                implementation(libs.turbine.test)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.database)
            }
        }

        val androidTest by getting

        val desktopMain by getting

        val desktopTest by getting
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

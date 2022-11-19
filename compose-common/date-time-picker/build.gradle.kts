import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

android {
    namespace = "work.racka.reluct.compose.common.date.time.picker"
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get() }
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:model"))
                implementation(project(":compose-common:pager"))
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.uiTooling)
                implementation(compose.foundation)
                implementation(compose.animation)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                @OptIn(ExperimentalComposeLibrary::class) implementation(compose.material3)
                implementation(compose.preview)

                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.kotlinx.date.time)
            }
        }

        val commonTest by getting

        val androidMain by getting {
            dependencies {
                implementation(libs.compose.ui)
                implementation(libs.compose.animation)
                implementation(libs.compose.material)
                implementation(libs.compose.material3)
                implementation(libs.compose.preview)
                implementation(libs.compose.foundation)
                implementation(libs.compose.foundation.layout)
            }
        }

        val androidTest by getting

        val desktopMain by getting {
            dependencies {}
        }

        val desktopTest by getting
    }
}

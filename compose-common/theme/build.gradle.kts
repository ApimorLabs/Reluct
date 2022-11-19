import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

android {
    namespace = "work.racka.reluct.compose.common.theme"
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)
                @OptIn(ExperimentalComposeLibrary::class) implementation(compose.material3)
            }
        }

        val commonTest by getting

        val androidMain by getting

        val androidTest by getting

        val desktopMain by getting

        val desktopTest by getting
    }
}
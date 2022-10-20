plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose") version Versions.composeDesktop
}

android {
    namespace = "work.racka.reluct.common.features.dashboard"
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = Versions.composeCompiler }
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:domain"))
                implementation(project(":common:model"))
                implementation(project(":common:mvvm-core"))
                implementation(project(":common:persistence:settings"))

                // Features imported
                implementation(project(":common:features:screen-time"))
                implementation(project(":common:features:tasks"))

                implementation(Dependencies.Kotlin.Coroutines.core)
                implementation(Dependencies.Koin.core)
                implementation(Dependencies.Log.kermit)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(Dependencies.Mockk.core)
                implementation(Dependencies.Mockk.commonMultiplatform)
                implementation(Dependencies.Koin.test)
                implementation(Dependencies.Kotlin.Coroutines.test)
                implementation(Dependencies.Squareup.Testing.turbine)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting

        val androidTest by getting

        val desktopMain by getting

        val desktopTest by getting
    }
}

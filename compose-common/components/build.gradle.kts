import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
}

android {
    namespace = "work.racka.reluct.compose.common.components"
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get() }
    dependencies {
        debugImplementation(libs.compose.tooling)
    }

    sourceSets["main"].apply {
        assets.srcDir(File(buildDir, "generated/moko/androidMain/assets"))
        res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
    }
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Dependency Modules
                implementation(project(":common:model"))
                api(project(":compose-common:charts"))
                implementation(project(":compose-common:date-time-picker"))
                implementation(project(":compose-common:pager"))
                implementation(project(":compose-common:theme"))

                implementation(compose.runtime)
                implementation(compose.preview)
                implementation(compose.ui)
                implementation(compose.uiTooling)
                implementation(compose.foundation)
                implementation(compose.animation)
                implementation(compose.animationGraphics)
                implementation(compose.materialIconsExtended)
                implementation(compose.material)
                @OptIn(ExperimentalComposeLibrary::class) implementation(compose.material3)

                api(libs.moko.resources.core)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.kotlinx.date.time)
                implementation(libs.kermit.log)
            }
        }

        val commonTest by getting

        val androidMain by getting {
            dependencies {
                // Compose
                implementation(libs.bundles.compose.core)
                api(libs.moko.resources.compose)
                implementation(libs.lottie.compose)
                // Palette
                implementation(libs.palette)
            }
        }

        val androidTest by getting

        val desktopMain by getting {
            dependencies {
                api(libs.moko.resources.compose)
            }
        }

        val desktopTest by getting
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "work.racka.reluct.compose.common.components"
    multiplatformResourcesClassName = "SharedRes"
}

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_11.majorVersion
        }
        withJava()
    }
    sourceSets {
        val desktopMain by getting {
            dependencies {
                // Project modules
                implementation(project(":common:di-integration"))
                implementation(project(":common:features:dashboard"))
                implementation(project(":common:features:goals"))
                implementation(project(":common:features:onboarding"))
                implementation(project(":common:features:screen-time"))
                implementation(project(":common:features:settings"))
                implementation(project(":common:features:tasks"))
                implementation(project(":common:model"))
                implementation(project(":common:mvvm-core"))
                implementation(project(":compose-common:components"))
                implementation(project(":compose-common:theme"))

                // Compose
                implementation(compose.desktop.currentOs)
                implementation(compose.ui)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.desktop.components.splitPane)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.animation)
                implementation(compose.animationGraphics)

                // Navigation
                implementation(libs.decompose.core)
                implementation(libs.decompose.compose.jb)

                // Other Dependencies
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.kotlinx.date.time)
                implementation(libs.koin.core)
                implementation(libs.jna.platform)
                implementation(libs.kermit.log)
            }
        }
        val desktopTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "work.racka.reluct.MainKt"

        val iconsRoot = project.file("src/desktopMain/resources/icons/launcher")
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = libs.versions.config.desktop.packageName.get()
            packageVersion = libs.versions.config.desktop.current.get()

            linux {
                iconFile.set(iconsRoot.resolve("linux.png"))
            }

            windows {
                iconFile.set(iconsRoot.resolve("windows.ico"))
            }

            macOS {
                iconFile.set(iconsRoot.resolve("macos.icns"))
            }
        }
    }
}

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
                implementation(project(":common:model"))
                implementation(project(":common:di-integration"))
                implementation(project(":compose-common:components"))

                implementation(compose.desktop.currentOs)
                implementation(compose.ui)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.desktop.components.splitPane)
                implementation(compose.materialIconsExtended)

                implementation(libs.kotlinx.date.time)
                implementation(libs.koin.core)
                implementation(libs.jna.platform)
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

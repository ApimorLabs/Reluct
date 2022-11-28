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
                implementation(project(":common:core-navigation"))
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
                implementation(libs.coroutines.core)
                implementation(libs.coroutines.swing)
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
        /*
        * Its unreliable. Don't run release tasks for now.
        * Wait until fixed: https://github.com/JetBrains/compose-jb/issues/2393
        */

        buildTypes.release.proguard {
            configurationFiles.from(project.file("compose-desktop.pro"))
            obfuscate.set(true)
            version.set("7.3.0")
        }

        val iconsRoot = project.file("src/desktopMain/resources/icons/launcher")
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageVersion = libs.versions.config.desktop.current.get()
            packageName = "Reluct"
            description = "Tasks Manager and Digital Wellbeing"
            copyright = "© 2022 RackApps. All rights reserved."
            vendor = "RackaApps"
            version = libs.versions.config.desktop.current.get()
            licenseFile.set(rootProject.file("LICENSE"))

            modules("java.net.http", "java.sql")

            linux {
                iconFile.set(iconsRoot.resolve("linux.png"))
                debMaintainer = "rackaapps@gmail.com"
                menuGroup = packageName
                appCategory = "Productivity"
            }

            windows {
                iconFile.set(iconsRoot.resolve("windows.ico"))
                shortcut = true
                menuGroup = packageName
                //https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                upgradeUuid = "791AC64E-C9A7-4CBF-A1C4-AFE5CFFDDDFA"
            }

            macOS {
                iconFile.set(iconsRoot.resolve("macos.icns"))
                bundleID = libs.versions.config.desktop.packageName.get()
                appCategory = "public.app-category.productivity"
            }
        }
    }
}

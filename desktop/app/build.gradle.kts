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
        // TODO: Fix on Electric Eel Beta03: https://issuetracker.google.com/issues/248593403
        //withJava()
    }
    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(project(":common:model"))
                implementation(project(":common:di-integration"))

                implementation(compose.desktop.currentOs)
                implementation(compose.ui)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.desktop.components.splitPane)
                implementation(compose.materialIconsExtended)

                implementation(libs.kotlinx.date.time)
                implementation(libs.koin.core)
            }
        }
        val desktopTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "work.racka.reluct.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = libs.versions.config.desktop.packageName.get()
            packageVersion = libs.versions.config.desktop.current.get()
        }
    }
}
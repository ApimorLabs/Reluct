import org.jetbrains.compose.compose
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

                implementation(compose.desktop.currentOs)
                implementation(Dependencies.Kotlin.dateTime)
                implementation(Dependencies.Koin.core)
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
            packageName = AppConfig.Desktop.packageName
            packageVersion = AppConfig.Desktop.version
        }
    }
}
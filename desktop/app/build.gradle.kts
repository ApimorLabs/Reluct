import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common:model"))
                implementation(project(":common:integration"))
                implementation(compose.desktop.currentOs)


                with(Dependencies.Koin) {
                    api(core)
                    api(test)
                }
            }
        }
        val jvmTest by getting
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
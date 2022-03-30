import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm("desktop")

    sourceSets["commonMain"].dependencies {
        implementation(Dependencies.Kotlin.Coroutines.core)

        with(Dependencies.RusshWolf.MultiplatformSettings) {
            implementation(core)
            implementation(noArg)
        }

        with(Dependencies.Koin) {
            api(core)
            api(test)
        }

        with(Dependencies.Log) {
            api(kermit)
        }
    }

    sourceSets["commonTest"].dependencies {
        implementation(Dependencies.RusshWolf.MultiplatformSettings.test)
        implementation(Dependencies.Koin.test)
        implementation(Dependencies.Kotlin.Coroutines.test)
        implementation(Dependencies.Squareup.Testing.turbine)
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

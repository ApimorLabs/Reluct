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
}

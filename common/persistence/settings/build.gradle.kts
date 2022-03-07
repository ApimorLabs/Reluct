plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm("desktop")

    sourceSets["commonMain"].dependencies {
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

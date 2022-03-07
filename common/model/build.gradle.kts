plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version Versions.kotlin
}

kotlin {
    jvm()
    sourceSets["commonMain"].dependencies {
        implementation(Dependencies.Kotlin.serializationCore)
    }
}

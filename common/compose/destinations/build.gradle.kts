import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()
    sourceSets["commonMain"].dependencies {
        implementation(compose.materialIconsExtended)
        implementation(compose.ui)
    }
}

plugins {
    id("com.android.library")
    kotlin("android")
}

android.apply {
    namespace = "work.racka.reluct.android.compose.theme"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    // Core Functionality
    implementation(libs.appCompat)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
}

plugins {
    id("com.android.library")
    kotlin("android")
}

android.apply {
    namespace = "work.racka.reluct.android.compose.charts"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.animation)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.preview)
    implementation(libs.compose.foundation)
    implementation(libs.compose.foundation.layout)

    // Testing Compose
    androidTestImplementation(libs.compose.junit)
    debugImplementation(libs.compose.tooling)

    // Timber - Logging
    implementation(libs.timber.log)
}

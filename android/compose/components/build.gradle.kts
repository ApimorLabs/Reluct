plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
}

android.apply {
    namespace = "work.racka.reluct.android.compose.components"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    // Dependency Modules
    implementation(project(":android:compose:charts"))
    implementation(project(":android:compose:theme"))
    implementation(project(":common:model"))

    // Compose
    // Compose
    implementation(libs.bundles.compose.core)

    // Testing Compose
    androidTestImplementation(libs.compose.junit)
    debugImplementation(libs.compose.tooling)

    // DateTime
    implementation(libs.kotlinx.date.time)

    // Accompanist: Keep an eye out for deprecated features
    implementation(libs.accompanist.pager)

    // Palette
    implementation(libs.palette)
    // Lottie - Compose
    implementation(libs.lottie.compose)
    // Timber - Logging
    implementation(libs.timber.log)
}

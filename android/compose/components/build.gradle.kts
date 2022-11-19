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
    implementation(project(":common:model"))
    api(project(":compose-common:charts"))
    implementation(project(":compose-common:date-time-picker"))
    implementation(project(":compose-common:pager"))
    implementation(project(":compose-common:theme"))

    // Compose
    // Compose
    implementation(libs.bundles.compose.core)

    // Testing Compose
    androidTestImplementation(libs.compose.junit)
    debugImplementation(libs.compose.tooling)

    // DateTime
    implementation(libs.kotlinx.date.time)

    // Palette
    implementation(libs.palette)
    // Lottie - Compose
    implementation(libs.lottie.compose)
    // Timber - Logging
    implementation(libs.timber.log)
}

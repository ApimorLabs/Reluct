plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("plugin.serialization")
}

android.apply {
    namespace = "work.racka.reluct.android.widgets"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    // Dependency Modules
    implementation(project(":common:core-navigation"))
    implementation(project(":common:domain"))
    implementation(project(":common:model"))
    implementation(project(":compose-common:components"))
    implementation(project(":compose-common:theme"))

    // Core Functionality
    implementation(libs.core.ktx)
    implementation(libs.google.material)

    implementation(libs.kotlinx.serialization.json)

    // Glance
    implementation(libs.compose.glance)

    // Koin
    implementation(libs.koin.core)

    // Testing
    testImplementation(libs.junit.core)
    testImplementation(libs.junit.test)
    testImplementation(libs.junit.test.ktx)
    androidTestImplementation(libs.junit.test)

    testImplementation(libs.android.test.arch.core)
    androidTestImplementation(libs.android.test.arch.core)
    androidTestImplementation(libs.android.test.core)
}

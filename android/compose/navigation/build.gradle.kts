plugins {
    id("com.android.library")
    kotlin("android")
}

android.apply {
    namespace = "work.racka.reluct.android.compose.navigation"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    // Dependency Modules
    api(project(":common:core-navigation"))
    implementation(project(":common:model"))
    implementation(project(":android:compose:components"))
    implementation(project(":android:compose:theme"))
    implementation(project(":android:screens"))

    // Core Functionality
    /*
    with(Dependencies.Android.Essential) {
        implementation(coreKtx)
        implementation(material)
    }
    */

    // Compose
    implementation(libs.bundles.compose.core)

    // Testing Compose
    androidTestImplementation(libs.compose.junit)
    debugImplementation(libs.compose.tooling)

    // Accompanist
    implementation(libs.accompanist.nav.animations)

    // Timber - Logging
    implementation(libs.timber.log)
}

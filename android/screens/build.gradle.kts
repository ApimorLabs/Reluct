plugins {
    id("com.android.library")
    kotlin("android")
}

android.apply {
    namespace = "work.racka.reluct.android.screens"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    // Dependency Modules
    implementation(project(":common:billing"))
    implementation(project(":common:model"))
    implementation(project(":common:mvvm-core"))
    implementation(project(":common:features:dashboard"))
    implementation(project(":common:features:goals"))
    implementation(project(":common:features:onboarding"))
    implementation(project(":common:features:screen-time"))
    implementation(project(":common:features:settings"))
    implementation(project(":common:features:tasks"))
    implementation(project(":compose-common:components"))
    implementation(project(":compose-common:theme"))

    // Core Functionality
    /*
    with(Dependencies.Android.Essential) {
        implementation(coreKtx)
        implementation(material)
    }
    */

    // Testing
    testImplementation(libs.junit.test)
    testImplementation(libs.junit.test.ktx)
    androidTestImplementation(libs.junit.test)

    testImplementation(libs.android.test.arch.core)
    androidTestImplementation(libs.android.test.arch.core)
    androidTestImplementation(libs.android.test.core)

    // Compose
    implementation(libs.bundles.compose.core)
    implementation(libs.lifecycle.compose.runtime)
    androidTestImplementation(libs.compose.junit)
    androidTestImplementation(libs.compose.tooling)

    // Palette
    implementation(libs.palette)

    // RevenueCat
    implementation(libs.revenueCat.android)

    // Timber - Logging
    implementation(libs.timber.log)
}

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
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
}

dependencies {
    // Dependency Modules
    implementation(project(":common:model"))
    implementation(project(":common:mvvm-core"))
    implementation(project(":android:compose:theme"))
    implementation(project(":android:compose:components"))
    implementation(project(":common:features:dashboard"))
    implementation(project(":common:features:screen-time"))
    implementation(project(":common:features:tasks"))

    // Core Functionality
    /*
    with(Dependencies.Android.Essential) {
        implementation(coreKtx)
        implementation(material)
    }
    */

    // Testing
    with(Dependencies.Android.JUnit) {
        testImplementation(core)
        testImplementation(test)
        testImplementation(testExtKtx)
        androidTestImplementation(test)
    }

    with(Dependencies.Android.TestCore) {
        testImplementation(testArch)
        androidTestImplementation(testArch)
        androidTestImplementation(testKtx)
    }

    // Compose
    with(Dependencies.Android.Compose) {
        implementation(ui)
        implementation(animation)
        implementation(material)
        implementation(preview)
        implementation(activity)
        implementation(viewModel)
        implementation(navigation)
        implementation(materialIconsCore)
        implementation(materialIconsExtended)
        implementation(foundation)
        implementation(foundationLayout)
        implementation(constraintLayout)
        implementation(material3)

        // Testing Compose
        androidTestImplementation(junit)
        debugImplementation(tooling)
    }

    // Koin
    with(Dependencies.Koin) {
        implementation(compose)
    }

    // Coil Image loader
    implementation(Dependencies.Android.Coil.image)

    // Accompanist
    with(Dependencies.Android.Accompanist) {
        implementation(navigationAnimations)
    }

    with(Dependencies.Android.Extras) {
        // Palette
        implementation(palette)
    }
    // Timber - Logging
    implementation(Dependencies.Log.timber)
}

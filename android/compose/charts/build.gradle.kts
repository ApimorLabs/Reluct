plugins {
    id("com.android.library")
    kotlin("android")
}

android.apply {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    // Dependency Modules

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
        implementation(foundation)
        implementation(foundationLayout)
        implementation(material3)

        // Testing Compose
        androidTestImplementation(junit)
        debugImplementation(tooling)
    }

    // Timber - Logging
    implementation(Dependencies.Log.timber)
}

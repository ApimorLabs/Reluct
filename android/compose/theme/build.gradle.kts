plugins {
    id("com.android.library")
    kotlin("android")
}

android.apply {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
}

dependencies {
    // Core Functionality
    with(Dependencies.Android.Essential) {
        implementation(appCompat)
    }

    // Compose
    with(Dependencies.Android.Compose) {
        implementation(ui)
        implementation(material)
        implementation(materialIconsCore)
        implementation(materialIconsExtended)
        implementation(foundation)
        implementation(material3)
    }
}

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

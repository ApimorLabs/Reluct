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

    // Compose
    with(Dependencies.Android.Compose) {
        implementation(materialIconsCore)
        implementation(materialIconsExtended)
    }
}

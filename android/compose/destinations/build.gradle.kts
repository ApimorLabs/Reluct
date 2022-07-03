plugins {
    id("com.android.library")
    kotlin("android")
}

android.apply {
    namespace = "work.racka.reluct.android.compose.destinations"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
}

dependencies {

    // Compose
    with(Dependencies.Android.Compose) {
        implementation(materialIconsCore)
        implementation(materialIconsExtended)
    }
}

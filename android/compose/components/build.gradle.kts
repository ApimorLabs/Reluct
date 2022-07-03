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
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
}

dependencies {
    // Dependency Modules
    implementation(project(":android:compose:charts"))
    implementation(project(":android:compose:destinations"))
    implementation(project(":android:compose:theme"))
    implementation(project(":common:model"))

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
        implementation(materialIconsCore)
        implementation(materialIconsExtended)
        implementation(navigation)
        implementation(foundation)
        implementation(foundationLayout)
        implementation(constraintLayout)
        implementation(material3)

        // Testing Compose
        androidTestImplementation(junit)
        debugImplementation(tooling)
    }

    // DateTime
    implementation(Dependencies.Kotlin.dateTime)

    // Coil Image loader
    implementation(Dependencies.Android.Coil.image)

    // Accompanist: Keep an eye out for deprecated features
    with(Dependencies.Android.Accompanist) {
        implementation(pager)
    }

    with(Dependencies.Android.Extras) {
        // Palette
        implementation(palette)
        // Lottie - Compose
        implementation(lottieCompose)
    }
    // Timber - Logging
    implementation(Dependencies.Log.timber)
}

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
    implementation(project(":common:billing"))
    implementation(project(":common:model"))
    implementation(project(":common:mvvm-core"))
    implementation(project(":android:compose:theme"))
    implementation(project(":android:compose:components"))
    implementation(project(":common:features:dashboard"))
    implementation(project(":common:features:goals"))
    implementation(project(":common:features:onboarding"))
    implementation(project(":common:features:screen-time"))
    implementation(project(":common:features:settings"))
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
        implementation(activity)
        implementation(animation)
        implementation(material)
        implementation(preview)
        implementation(materialIconsCore)
        implementation(materialIconsExtended)
        implementation(foundation)
        implementation(foundationLayout)
        implementation(material3)

        // Testing Compose
        androidTestImplementation(junit)
        debugImplementation(tooling)
    }

    // Coil Image loader
    implementation(Dependencies.Android.Coil.image)

    with(Dependencies.Android.Extras) {
        // Palette
        implementation(palette)
    }

    // RevenueCat
    implementation(Dependencies.Revenuecat.android)

    // Timber - Logging
    implementation(Dependencies.Log.timber)
}

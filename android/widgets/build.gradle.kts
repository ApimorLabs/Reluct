plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version Versions.kotlin
}

android.apply {
    namespace = "work.racka.reluct.android.widgets"

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
    implementation(project(":android:compose:theme"))
    implementation(project(":android:compose:components"))
    implementation(project(":android:compose:navigation"))
    implementation(project(":common:data"))

    // Core Functionality
    with(Dependencies.Android.Essential) {
        implementation(coreKtx)
        implementation(material)
    }

    implementation(Dependencies.Kotlin.kotlinJsonSerialization)

    // Glance
    implementation(Dependencies.Android.Extras.glanceAppWidget)

    // Koin
    implementation(Dependencies.Koin.core)

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
}
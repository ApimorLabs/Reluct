plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/main/AndroidManifest.xml")
            res.srcDirs("src/main/res")
        }
    }
}

dependencies {
    // Dependency Modules
    implementation(project(":common:model"))
    implementation(project(":android:compose:theme"))

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

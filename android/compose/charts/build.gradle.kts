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

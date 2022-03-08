import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        properties.load(
            project.rootProject.file("local.properties")
                .reader()
        )

        buildConfigField(
            "String",
            "revenuecat_key",
            properties.getProperty("revenuecat_key")
        )
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    packagingOptions {
        resources {
            excludes += mutableSetOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/licenses/ASM"
            )
            // Fixes conflicts caused by ByteBuddy library used in
            // coroutines-debug and mockito
            pickFirsts += mutableSetOf(
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86/attach_hotspot_windows.dll"
            )
        }
    }
}

dependencies {
    // Dependency Modules
    implementation(project(":common:model"))
    implementation(project(":common:integration"))
    implementation(project(":android:compose:components"))
    implementation(project(":android:compose:charts"))

    // Core Functionality
    with(Dependencies.Android.Essential) {
        implementation(coreKtx)
        implementation(appCompat)
        implementation(material)
        implementation(lifecycleRuntimeKtx)
    }

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

    testImplementation(Dependencies.Squareup.Testing.turbine)
    testImplementation(Dependencies.Kotlin.Coroutines.test)

    androidTestImplementation(Dependencies.Android.Espresso.core)
    androidTestImplementation(Dependencies.Squareup.Testing.turbine)

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

    // Hilt
    with(Dependencies.Android.Hilt) {
        implementation(core)
        implementation(navigationCompose)
        kapt(compiler)

        // Local Unit Tests
        testImplementation(test)
        kaptTest(compiler)
        // Instrumentation Test
        androidTestImplementation(test)
        kaptAndroidTest(compiler)
    }

    // Coil Image loader
    implementation(Dependencies.Android.Coil.image)

    // Accompanist
    with(Dependencies.Android.Accompanist) {
        implementation(insets)
        implementation(navigationAnimations)
    }

    with(Dependencies.Android.Extras) {
        // Preferences DataStore
        implementation(prefDataStore)

        // Splash Screen
        implementation(splashScreenCore)

        // Palette
        implementation(palette)
    }
    // Timber - Logging
    implementation(Dependencies.Log.timber)
}

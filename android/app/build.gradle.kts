import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    kotlin("android")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
    implementation(project(":android:compose:navigation"))
    implementation(project(":android:compose:theme"))
    implementation(project(":common:di-integration"))
    implementation(project(":common:persistence:settings"))

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

    // Compose
    with(Dependencies.Android.Compose) {
        implementation(ui)
        implementation(activity)
        implementation(foundation)

        // Testing Compose
        androidTestImplementation(junit)
        debugImplementation(tooling)
    }

    // Koin DI
    implementation(Dependencies.Koin.android)
    // Accompanist
    implementation(Dependencies.Android.Accompanist.systemUiController)
    // Splash Screen
    implementation(Dependencies.Android.Extras.splashScreenCore)
    // Timber - Logging
    implementation(Dependencies.Log.timber)
}

import org.jetbrains.kotlin.konan.properties.Properties

val desugaringVersion = libs.versions.desugaring.get()

plugins {
    kotlin("android")
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "work.racka.reluct"

    compileSdk = libs.versions.config.android.compilesdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.config.android.applicationId.get()
        minSdk = libs.versions.config.android.minsdk.get().toInt()
        targetSdk = libs.versions.config.android.targetsdk.get().toInt()
        versionCode = libs.versions.config.android.versioncode.get().toInt()
        versionName = libs.versions.config.android.versionName.get()

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
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("benchmark") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "benchmark-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
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
    // Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugaringVersion")

    // Dependency Modules
    implementation(project(":android:navigation"))
    implementation(project(":compose-common:theme"))
    implementation(project(":android:widgets"))
    implementation(project(":common:di-integration"))
    implementation(project(":common:features:screen-time"))
    implementation(project(":common:persistence:settings"))

    // Core Functionality
    implementation(libs.core.ktx)
    implementation(libs.appCompat)
    implementation(libs.google.material)
    implementation(libs.viewmodel.core)
    implementation(libs.androidx.profileinstaller)

    // Testing
    testImplementation(libs.junit.core)
    testImplementation(libs.junit.test)
    testImplementation(libs.junit.test.ktx)
    androidTestImplementation(libs.junit.test)

    testImplementation(libs.android.test.arch.core)
    androidTestImplementation(libs.android.test.arch.core)
    androidTestImplementation(libs.android.test.core)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.activity.compose)
    implementation(libs.compose.foundation)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    // Testing Compose
    androidTestImplementation(libs.compose.junit)
    debugImplementation(libs.compose.tooling)

    // Koin DI
    implementation(libs.koin.android)
    implementation(libs.koin.android.workmanager)
    // Accompanist
    implementation(libs.accompanist.system.ui.controller)
    // Splash Screen
    implementation(libs.splash.screen.core)
    // Timber - Logging
    implementation(libs.timber.log)

    // Revenue Cat - Billing
    implementation(libs.revenueCat.android)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // Leaks
    debugImplementation(libs.leakcanary.android)
}

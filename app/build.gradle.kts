
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
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.material)
    implementation(Dependencies.lifecycleRuntimeKtx)

    // Testing
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.testArchCore)
    testImplementation(Dependencies.junitTest)
    testImplementation(Dependencies.testExtJUnitKtx)
    testImplementation(Dependencies.mockitoInline)
    testImplementation(Dependencies.mockitoKotlin)
    testImplementation(Dependencies.robolectric)
    testImplementation(Dependencies.turbine)
    testImplementation(Dependencies.coroutineTest)

    androidTestImplementation(Dependencies.junitTest)
    androidTestImplementation(Dependencies.espressoCore)
    androidTestImplementation(Dependencies.testCoreKtx)
    androidTestImplementation(Dependencies.testArchCore)
    androidTestImplementation(Dependencies.mockitoAndroid)
    androidTestImplementation(Dependencies.mockitoKotlin)
    androidTestImplementation(Dependencies.turbine)

    // Hilt Testing
    // Local Unit Tests
    testImplementation(Dependencies.hiltTest)
    kaptTest(Dependencies.hiltCompiler)
    // Instrumentation Test
    androidTestImplementation(Dependencies.hiltTest)
    kaptAndroidTest(Dependencies.hiltCompiler)

    // Testing Compose
    androidTestImplementation(Dependencies.junitCompose)
    debugImplementation(Dependencies.composeTooling)

    // Compose
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeAnimation)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composePreview)
    implementation(Dependencies.composeActivity)
    implementation(Dependencies.composeViewModel)
    implementation(Dependencies.composeNavigation)
    implementation(Dependencies.composeMaterialIconsCore)
    implementation(Dependencies.composeMaterialIconsExtended)
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeFoundationLayout)
    implementation(Dependencies.composeConstraintLayout)

    // Retrofit
    implementation(Dependencies.retrofit)

    // Moshi
    implementation(Dependencies.moshi)
    implementation(Dependencies.moshiKotlin)

    // Timber
    implementation(Dependencies.timber)

    // Hilt
    implementation(Dependencies.hilt)
    implementation(Dependencies.hiltNavigationCompose)
    kapt(Dependencies.hiltCompiler)

    // Coil Image loader
    implementation(Dependencies.coilImage)

    // Accompanist
    implementation(Dependencies.accompanistInsets)
    implementation(Dependencies.accompanistNavigationAnimations)

    // Room database
    implementation(Dependencies.roomRuntime)
    implementation(Dependencies.roomKtx)
    kapt(Dependencies.roomCompiler)

    // Room test
    testImplementation(Dependencies.roomTest)

    // Preferences DataStore
    implementation(Dependencies.prefDataStore)

    // Splash Screen
    implementation(Dependencies.splashScreenCore)
}

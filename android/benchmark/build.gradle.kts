plugins {
    id("com.android.test")
    kotlin("android")
}

android {
    namespace = "work.racka.reluct.android.benchmark"

    compileSdk = libs.versions.config.android.compilesdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.config.android.minsdk.get().toInt()
        targetSdk = libs.versions.config.android.targetsdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "../app/benchmark-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    targetProjectPath = ":android:app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(libs.junit.test)
    implementation(libs.junit.test.ktx)
    implementation(libs.espresso.core)
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.test.uiautomator)
}

androidComponents {
    beforeVariants(selector().all()) {
        it.enabled = it.buildType == "benchmark"
    }
}
//package plugins
//
//import gradle.kotlin.dsl.accessors._5946a4d43cee9e5f506e97277a76864d.android
//import org.gradle.kotlin.dsl.kotlin
//
//plugins {
//    kotlin("multiplatform")
//    id("com.android.library")
//}
//
//android {
//    compileSdk = AppConfig.compileSdkVersion
//
//    defaultConfig {
//        minSdk = AppConfig.minSdkVersion
//        targetSdk = AppConfig.targetSdkVersion
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables {
//            useSupportLibrary = true
//        }
//    }
//
//    buildTypes {
//        getByName("release") {
//            isMinifyEnabled = true
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//
//    sourceSets {
//        named("main") {
//            manifest.srcFile("src/androidMain/AndroidManifest.xml")
//            res.srcDirs("src/androidMain/res")
//        }
//    }
//}
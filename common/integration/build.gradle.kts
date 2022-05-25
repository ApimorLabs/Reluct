plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:data"))
                implementation(project(":common:model"))
                implementation(project(":common:persistence:database"))
                implementation(project(":common:persistence:settings"))
                implementation(project(":common:features:tasks"))
                implementation(project(":common:features:goals"))

                implementation(Dependencies.OrbitMVI.core) // Should be removed
                implementation(Dependencies.Squareup.SQLDelight.coroutineExtensions)
                implementation(Dependencies.Koin.core)
                implementation(Dependencies.Log.kermit)
            }
        }

        val commonTest by getting

        val androidMain by getting {
            dependencies {

            }
        }

        val androidTest by getting

        val desktopMain by getting {
            dependencies {
            }
        }

        val desktopTest by getting
    }
}

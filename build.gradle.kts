import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        // Leaving this plugin here so it can be automatically
        // updated by Android Studio
        classpath("com.android.tools.build:gradle:7.2.0")

        classpath(BuildPlugins.kotlin)
        classpath(BuildPlugins.sqlDelight)
        //classpath(BuildPlugins.composeDesktop)
        //classpath(BuildPlugins.daggerHilt)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://www.jitpack.io")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
    }

    tasks.withType<KotlinJvmCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.majorVersion
    }
}

subprojects {
    afterEvaluate {
        val kmpExtension = project.extensions.findByType<KotlinMultiplatformExtension>()

        // Check if the subproject is KMP or a normal Android subproject
        val isAndroidKMP = kmpExtension != null

        // Remove unused Kotlin Multiplatform source sets
        kmpExtension?.let { ext ->
            ext.sourceSets.removeAll { sourceSet ->
                setOf(
                    "androidAndroidTestRelease",
                    "androidTestFixtures",
                    "androidTestFixturesDebug",
                    "androidTestFixturesRelease",
                ).contains(sourceSet.name)
            }
        }

        // Provide default library configurations for each Android source
        project.extensions.findByType<com.android.build.gradle.LibraryExtension>()?.apply {
            compileSdk = AppConfig.compileSdkVersion

            defaultConfig {
                minSdk = AppConfig.minSdkVersion
                targetSdk = AppConfig.targetSdkVersion

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                vectorDrawables {
                    useSupportLibrary = true
                }

            }

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = true
                }
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }

            // If the subproject is KMP we need to check in "androidMain" for manifest files
            // Else we check in the normal "main" folder
            if (isAndroidKMP) {
                sourceSets {
                    named("main") {
                        manifest.srcFile("src/androidMain/AndroidManifest.xml")
                        res.srcDirs("src/androidMain/res")
                    }
                }
            } else {
                sourceSets {
                    named("main") {
                        manifest.srcFile("src/main/AndroidManifest.xml")
                        res.srcDirs("src/main/res")
                    }
                }
            }
        }
    }
}
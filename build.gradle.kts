import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    // TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.detekt.gradle)
}

buildscript {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.compose.multiplatform.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.kotlin.serialization.plugin)
        classpath(libs.sqldelight.plugin)
        classpath(libs.google.services.plugin)
        classpath(libs.firebase.crashlytics.plugin)

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

    // Detekt
    apply(plugin = "io.gitlab.arturbosch.detekt")

    beforeEvaluate {
        dependencies {
            detektPlugins(libs.detekt.formatting)
            detektPlugins(libs.detekt.rule.twitter.compose)
        }

        // Filter out Detekt from check task
        if (tasks.names.contains("check")) {
            tasks.named("check").configure {
                this.setDependsOn(this.dependsOn.filterNot {
                    it is TaskProvider<*> && it.name == "detekt"
                })
            }
        }
    }

    afterEvaluate {
        /**
         * Start - Detekt Configuration for All sub projects
         */
        detekt {
            toolVersion = libs.versions.detekt.get()
            config = files("$rootDir/config/detekt/detekt.yml")
            buildUponDefaultConfig = true
        }

        tasks.withType<Detekt>().configureEach detekt@{
            exclude("**/generated/**", "**/resources/**", "**/build/**")
            include("**/kotlin/**", "**/java/**", "**/*.kt", "**/*.kts")
            /*setSource(
                files(
                    "src/main/java",
                    "src/test/java",
                    "src/main/kotlin",
                    "src/test/kotlin",
                    "gensrc/main/kotlin",
                )
            )*/

            basePath = rootProject.projectDir.absolutePath
            autoCorrect = true
            reports {
                xml.required.set(false)
                html.required.set(true)
                txt.required.set(false)
                sarif.required.set(false)
                md.required.set(false)

                html {
                    required.set(true)
                    outputLocation.set(
                        this@subprojects.layout.buildDirectory.file("reports/detekt.html")
                    )
                }
            }
        }
        /** End - Detek Config **/

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
            compileSdk = libs.versions.config.android.compilesdk.get().toInt()

            defaultConfig {
                minSdk = libs.versions.config.android.minsdk.get().toInt()
                targetSdk = libs.versions.config.android.targetsdk.get().toInt()

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                vectorDrawables {
                    useSupportLibrary = true
                }

                // So you can get the currently assigned App Version from your modules
                buildConfigField(
                    "String",
                    "appVersionName",
                    "\"${libs.versions.config.android.versionName.get()}\""
                )

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
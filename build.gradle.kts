import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
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
        classpath(libs.moko.resources.plugin)

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

val sarifReportMerge by tasks.registering(ReportMergeTask::class) {
    output.set(rootProject.buildDir.resolve("reports/detekt/merged_report.sarif"))
}

subprojects {

    /**
     * Start Configuring Detekt
     */
    coreDetektSetup()
    beforeEvaluate {
        dependencies {
            detektPlugins(libs.detekt.formatting)
            detektPlugins(libs.detekt.rule.twitter.compose)
        }
    }

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

/**
 * To run detekt simply:
 * 1. ./gradlew module:detekt for each module
 * 2. ./gradlew detekt for whole project
 */
fun Project.coreDetektSetup() {
    // Apply Plugin to sub-project
    apply(plugin = "io.gitlab.arturbosch.detekt")

    // Configure Detekt
    detekt {
        config = files("$rootDir/config/detekt/detekt.yml")
        buildUponDefaultConfig = true
        ignoredBuildTypes = listOf("release")
        source = files(
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_JAVA,
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_TEST_SRC_DIR_JAVA,
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_TEST_SRC_DIR_KOTLIN,
            // Kotlin Multiplatform
            "src/commonMain/kotlin",
            "src/androidMain/kotlin",
            "src/iosMain/kotlin",
            "src/jvmMain/kotlin",
            "src/desktopMain/kotlin",
            "src/jsMain/kotlin",
        )
        layout.projectDirectory.file("detekt-baseline.xml").asFile.run {
            if (exists()) baseline = this
        }
    }

    tasks.withType<Detekt>().configureEach detekt@{
        exclude("**/build/**", "**/generated/**", "**/resources/**")
        basePath = rootProject.projectDir.absolutePath
        autoCorrect = true // Auto corrects common formatting issues
        // Configure reports here
        reports {
            xml.required.set(false)
            txt.required.set(false)
            md.required.set(false)

            html {
                required.set(true)
                outputLocation.set(
                    layout.buildDirectory.file("reports/detekt.html")
                )
            }

            sarif.required.set(true)
        }

        // Merged Report
        finalizedBy(sarifReportMerge)
        sarifReportMerge.configure {
            input.from(this@detekt.sarifReportFile)
        }
    }

    tasks.withType<DetektCreateBaselineTask>().configureEach detekt@{
        exclude("**/build/**", "**/generated/**", "**/resources/**")
        basePath = rootProject.projectDir.absolutePath
        baseline.set(layout.projectDirectory.file("detekt-baseline.xml").asFile)
    }
}

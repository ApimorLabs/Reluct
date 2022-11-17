import extensions.androidGradle
import extensions.detektGradle
import quality.DetektSetup

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

buildscript {
    repositories {
        gradlePluginPortal()
        google()
    }

    dependencies {
        classpath(libs.androidGradle)
        classpath(libs.detektGradle)
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt")
}

project.beforeEvaluate {

}

fun helloConfig() {
    println("Hello")
}
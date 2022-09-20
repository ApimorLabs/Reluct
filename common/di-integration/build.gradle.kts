plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    namespace = "work.racka.reluct.common.di.integration"
}

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:app-usage-stats"))
                implementation(project(":common:authentication"))
                implementation(project(":common:data"))
                implementation(project(":common:features:dashboard"))
                implementation(project(":common:features:goals"))
                implementation(project(":common:features:screen-time"))
                implementation(project(":common:features:settings"))
                implementation(project(":common:features:tasks"))
                implementation(project(":common:model"))
                implementation(project(":common:persistence:database"))
                implementation(project(":common:persistence:settings"))
                implementation(project(":common:system-services"))

                implementation(Dependencies.Koin.core)
            }
        }

        val commonTest by getting

        val androidMain by getting

        val androidTest by getting

        val jvmMain by getting

        val jvmTest by getting
    }
}

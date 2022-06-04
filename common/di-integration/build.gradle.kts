plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:app-usage-stats"))
                implementation(project(":common:data"))
                implementation(project(":common:features:goals"))
                implementation(project(":common:features:screen-time"))
                implementation(project(":common:features:settings"))
                implementation(project(":common:features:tasks"))
                implementation(project(":common:model"))
                implementation(project(":common:persistence:database"))
                implementation(project(":common:persistence:settings"))

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

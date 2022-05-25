plugins {
    `kotlin-dsl`
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.majorVersion
        }
    }
}

repositories {
    google()
    mavenCentral()
}
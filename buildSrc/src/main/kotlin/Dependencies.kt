object Dependencies {

    object Jetbrains {
        val testCommon by lazy { "org.jetbrains.kotlin:kotlin-test-common:${Versions.kotlin}" }
        val testAnnotationsCommon by lazy { "org.jetbrains.kotlin:kotlin-test-annotations-common:${Versions.kotlin}" }

    }

    object Kotlin {
        val kotlinJunit by lazy { "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}" }
        val kotlinJsonSerialization by lazy { "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerialization}" }
        val serializationCore by lazy { "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinSerialization}" }

        val dateTime by lazy { "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDateTime}" }

        object Coroutines {
            private const val VERSION = "1.6.0"
            val core by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION" }
            val android by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-android:$VERSION" }
            val test by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutineTest}" }
        }
    }

    object Koin {
        val core by lazy { "io.insert-koin:koin-core:${Versions.koin}" }
        val test by lazy { "io.insert-koin:koin-test:${Versions.koin}" }
        val testJUnit4 by lazy { "io.insert-koin:koin-test-junit4:${Versions.koin}" }
        val android by lazy { "io.insert-koin:koin-android:${Versions.koin}" }
        val compose by lazy { "io.insert-koin:koin-androidx-compose:${Versions.koin}" }
    }

    object Ktor {
        val ktorCore by lazy { "io.ktor:ktor-client-core:${Versions.ktor}" }
        val ktorAndroidEngine by lazy { "io.ktor:ktor-client-android:${Versions.ktor}" }
        val ktorOkHttpEngine by lazy { "io.ktor:ktor-client-okhttp:${Versions.ktor}" }
        val ktorJavaEngine by lazy { "io.ktor:ktor-client-java:${Versions.ktor}" }
        val ktorSerialization by lazy { "io.ktor:ktor-client-serialization:${Versions.ktor}" }
        val ktorLogging by lazy { "io.ktor:ktor-client-logging:${Versions.ktor}" }
        val clientJson by lazy { "io.ktor:ktor-client-json:${Versions.ktor}" }
        val json by lazy { "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}" }
        val contentNegotiation by lazy { "io.ktor:ktor-client-content-negotiation:${Versions.ktor}" }
    }

    object OrbitMVI {
        val core by lazy { "org.orbit-mvi:orbit-core:${Versions.orbitMVI}" }
        val androidViewModel by lazy { "org.orbit-mvi:orbit-viewmodel:${Versions.orbitMVI}" }
        val test by lazy { "org.orbit-mvi:orbit-test:${Versions.orbitMVI}" }
    }

    object ArkIvanov {

        object Decompose {
            private const val VERSION = "0.3.1"
            val decompose by lazy { "com.arkivanov.decompose:decompose:$VERSION" }
            val extensionsCompose by lazy {
                "com.arkivanov.decompose:extensions-compose-jetbrains:$VERSION"
            }
        }
    }

    object RusshWolf {
        object MultiplatformSettings {
            val core by lazy { "com.russhwolf:multiplatform-settings:${Versions.multiplatformSettings}" }
            val noArg by lazy { "com.russhwolf:multiplatform-settings-no-arg:${Versions.multiplatformSettings}" }
            val coroutines by lazy { "com.russhwolf:multiplatform-settings-coroutines:${Versions.multiplatformSettings}" }
        }
    }

    object Revenuecat {
        val android by lazy { "com.revenuecat.purchases:purchases:${Versions.revenuecat}" }
    }

    object Squareup {
        object SQLDelight {
            val runtime by lazy { "com.squareup.sqldelight:runtime:${Versions.sqlDelight}" }
            val coroutineExtensions by lazy { "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}" }
            val androidDriver by lazy { "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}" }
            val sqliteDriver by lazy { "com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelight}" }
        }

        object Testing {
            val turbine by lazy { "app.cash.turbine:turbine:${Versions.turbine}" }
        }
    }

    object Log {
        val slf4j by lazy { "org.slf4j:slf4j-simple:${Versions.slf4j}" }
        val logback by lazy { "ch.qos.logback:logback-classic:${Versions.logback}" }
        val kermit by lazy { "co.touchlab:kermit:${Versions.kermit}" }
        val timber by lazy { "com.jakewharton.timber:timber:${Versions.timber}" }
    }

    object Android {

        object Essential {
            val coreKtx by lazy { "androidx.core:core-ktx:${Versions.coreKtx}" }
            val appCompat by lazy { "androidx.appcompat:appcompat:${Versions.appCompat}" }
            val material by lazy { "com.google.android.material:material:${Versions.material}" }

            // Lifecycle components
            val lifecycleRuntimeKtx by lazy {
                "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"
            }
        }

        // Testing
        object JUnit {
            val core by lazy { "junit:junit:${Versions.junit}" }
            val test by lazy { "androidx.test.ext:junit:${Versions.junitTest}" }
            val testExtKtx by lazy { "androidx.test.ext:junit-ktx:${Versions.testExtJUnit}" }
        }

        object Espresso {
            val core by lazy { "androidx.test.espresso:espresso-core:${Versions.espressoTest}" }
        }

        object TestCore {
            val testKtx by lazy { "androidx.test:core-ktx:${Versions.testCore}" }
            val testArch by lazy { "androidx.arch.core:core-testing:${Versions.testArchCore}" }
        }

        // Compose
        object Compose {
            val ui by lazy { "androidx.compose.ui:ui:${Versions.compose}" }
            val material by lazy { "androidx.compose.material:material:${Versions.compose}" }
            val material3 by lazy { "androidx.compose.material3:material3:${Versions.composeMaterial3}" }
            val animation by lazy { "androidx.compose.animation:animation:${Versions.compose}" }
            val preview by lazy { "androidx.compose.ui:ui-tooling-preview:${Versions.compose}" }
            val activity by lazy { "androidx.activity:activity-compose:${Versions.activityCompose}" }
            val viewModel by lazy { "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleViewModelCompose}" }
            val navigation by lazy { "androidx.navigation:navigation-compose:${Versions.composeNavigation}" }
            val materialIconsCore by lazy { "androidx.compose.material:material-icons-core:${Versions.compose}" }
            val materialIconsExtended by lazy { "androidx.compose.material:material-icons-extended:${Versions.compose}" }
            val foundation by lazy { "androidx.compose.foundation:foundation:${Versions.compose}" }
            val foundationLayout by lazy { "androidx.compose.foundation:foundation-layout:${Versions.compose}" }
            val constraintLayout by lazy { "androidx.constraintlayout:constraintlayout-compose:${Versions.composeConstraintLayout}" }

            // Compose Testing
            val junit by lazy { "androidx.compose.ui:ui-test-junit4:${Versions.compose}" }
            val tooling by lazy { "androidx.compose.ui:ui-tooling:${Versions.compose}" }
        }

        // Dagger Hilt
        object Hilt {
            val core by lazy { "com.google.dagger:hilt-android:${Versions.hilt}" }
            val navigationCompose by lazy {
                "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationCompose}"
            }
            val compiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.hilt}" }

            // Hilt testing
            val test by lazy { "com.google.dagger:hilt-android-testing:${Versions.hilt}" }
        }

        // Coil image loader
        object Coil {
            val image by lazy { "io.coil-kt:coil-compose:${Versions.coilImage}" }
        }

        // Accompanist
        object Accompanist {
            @Deprecated("Replace with Foundation Layout insets")
            val insets by lazy { "com.google.accompanist:accompanist-insets:${Versions.accompanist}" }
            val navigationAnimations by lazy {
                "com.google.accompanist:accompanist-navigation-animation:${Versions.accompanist}"
            }
        }

        object Extras {
            // Preferences DataStore
            val prefDataStore by lazy { "androidx.datastore:datastore-preferences:${Versions.dataStore}" }

            // Splash Screen
            val splashScreenCore by lazy { "androidx.core:core-splashscreen:${Versions.splashScreen}" }

            // Glance AppWidget - Early Snapshot
            val glanceAppWidget by lazy { "androidx.glance:glance-appwidget:${Versions.glanceAppWidget}" }

            // Palette
            val palette by lazy { "androidx.palette:palette-ktx:${Versions.palette}" }
        }
    }

}

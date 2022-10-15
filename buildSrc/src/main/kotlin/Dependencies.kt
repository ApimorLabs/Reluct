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
            val core by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}" }
            val android by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}" }
            val test by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}" }
            val playServices by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutines}" }
        }
    }

    object Koin {
        val core by lazy { "io.insert-koin:koin-core:${Versions.koin}" }
        val test by lazy { "io.insert-koin:koin-test:${Versions.koin}" }
        val testJUnit4 by lazy { "io.insert-koin:koin-test-junit4:${Versions.koin}" }
        val android by lazy { "io.insert-koin:koin-android:${Versions.koin}" }
        val androidWorkManager by lazy { "io.insert-koin:koin-androidx-workmanager:${Versions.koin}" }
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

    object ArkIvanov {

        object Decompose {
            private const val VERSION = "0.3.1"
            val decompose by lazy { "com.arkivanov.decompose:decompose:$VERSION" }
            val jetbrainsCompose by lazy {
                "com.arkivanov.decompose:extensions-compose-jetbrains:$VERSION"
            }
        }
    }

    object RusshWolf {
        object MultiplatformSettings {
            val core by lazy { "com.russhwolf:multiplatform-settings:${Versions.multiplatformSettings}" }
            val noArg by lazy { "com.russhwolf:multiplatform-settings-no-arg:${Versions.multiplatformSettings}" }
            val coroutines by lazy { "com.russhwolf:multiplatform-settings-coroutines:${Versions.multiplatformSettings}" }
            val test by lazy { "com.russhwolf:multiplatform-settings-test:${Versions.multiplatformSettings}" }
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

    object Vanpra {
        object ComposeMaterialDialogs {
            val dateTime by lazy { "io.github.vanpra.compose-material-dialogs:datetime:${Versions.composeMaterialDialogs}" }
        }
    }

    object Mockk {
        val core by lazy { "io.mockk:mockk:${Versions.mockk}" }
        val android by lazy { "io.mockk:mockk-android:${Versions.mockk}" }
        val jvm by lazy { "io.mockk:mockk-agent-jvm:${Versions.mockk}" }
        val commonMultiplatform by lazy { "io.mockk:mockk-common:${Versions.mockk}" }
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
            val saveState by lazy { "androidx.savedstate:savedstate-ktx:${Versions.saveState}" }
            val coreViewModel by lazy {
                "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
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
            val runtime by lazy { "androidx.compose.runtime:runtime:${Versions.compose}" }
            val ui by lazy { "androidx.compose.ui:ui:${Versions.compose}" }
            val material by lazy { "androidx.compose.material:material:${Versions.compose}" }
            val material3 by lazy { "androidx.compose.material3:material3:${Versions.composeMaterial3}" }
            val animation by lazy { "androidx.compose.animation:animation:${Versions.compose}" }
            val preview by lazy { "androidx.compose.ui:ui-tooling-preview:${Versions.compose}" }
            val activity by lazy { "androidx.activity:activity-compose:${Versions.activityCompose}" }
            val viewModel by lazy { "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}" }
            val navigation by lazy { "androidx.navigation:navigation-compose:${Versions.composeNavigation}" }
            val materialIconsCore by lazy { "androidx.compose.material:material-icons-core:${Versions.compose}" }
            val materialIconsExtended by lazy { "androidx.compose.material:material-icons-extended:${Versions.compose}" }
            val foundation by lazy { "androidx.compose.foundation:foundation:${Versions.compose}" }
            val foundationLayout by lazy { "androidx.compose.foundation:foundation-layout:${Versions.compose}" }
            val constraintLayout by lazy { "androidx.constraintlayout:constraintlayout-compose:${Versions.composeConstraintLayout}" }

            // Compose Testing
            val junit by lazy { "androidx.compose.ui:ui-test-junit4:${Versions.compose}" }
            val tooling by lazy { "androidx.compose.ui:ui-tooling:${Versions.compose}" }
            val toolingPreview by lazy { "androidx.compose.ui:ui-tooling-preview:${Versions.compose}" }
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
            val pager by lazy { "com.google.accompanist:accompanist-pager:${Versions.accompanist}" }
            val swipeRefresh by lazy { "com.google.accompanist:accompanist-swiperefresh:${Versions.accompanist}" }
            val systemUiController by lazy { "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}" }
        }

        // Firebase
        object Firebase {
            val bom by lazy { "com.google.firebase:firebase-bom:${Versions.firebaseBom}" }
            val analytics by lazy { "com.google.firebase:firebase-analytics-ktx" }
            val crashlytics by lazy { "com.google.firebase:firebase-crashlytics-ktx" }
            val auth by lazy { "com.google.firebase:firebase-auth-ktx" }
        }

        // Play Services
        object PlayServices {
            val auth by lazy { "com.google.android.gms:play-services-auth:${Versions.playServicesAuth}" }
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

            // Lottie animations
            val lottieCompose by lazy { "com.airbnb.android:lottie-compose:${Versions.lottie}" }
        }
    }

}

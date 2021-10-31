
object Dependencies {

    // Essentials
    val coreKtx by lazy { "androidx.core:core-ktx:${Versions.coreKtx}" }
    val appCompat by lazy { "androidx.appcompat:appcompat:${Versions.appCompat}" }
    val material by lazy { "com.google.android.material:material:${Versions.material}" }

    // Lifecycle components
    val lifecycleRuntimeKtx by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}" }

    // Testing
    val junit by lazy { "junit:junit:${Versions.junit}" }
    val junitTest by lazy { "androidx.test.ext:junit:${Versions.junitTest}" }
    val espressoCore by lazy { "androidx.test.espresso:espresso-core:${Versions.espressoTest}" }
    val testCoreKtx by lazy { "androidx.test:core-ktx:${Versions.testCore}" }
    val testArchCore by lazy { "androidx.arch.core:core-testing:${Versions.testArchCore}" }
    val testExtJUnitKtx by lazy { "androidx.test.ext:junit-ktx:${Versions.testExtJUnit}" }
    val mockitoInline by lazy { "org.mockito:mockito-inline:${Versions.mockito}" }
    val mockitoAndroid by lazy { "org.mockito:mockito-android:${Versions.mockito}" }
    val mockitoKotlin by lazy { "org.mockito.kotlin:mockito-kotlin:${Versions.mockitoKotlin}" }
    val robolectric by lazy { "org.robolectric:robolectric:${Versions.robolectric}" }
    val turbine by lazy { "app.cash.turbine:turbine:${Versions.turbine}" }
    val coroutineTest by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutineTest}" }

    // Compose Testing
    val junitCompose by lazy { "androidx.compose.ui:ui-test-junit4:${Versions.compose}" }
    val composeTooling by lazy { "androidx.compose.ui:ui-tooling:${Versions.compose}" }

    // Compose
    val composeUi by lazy { "androidx.compose.ui:ui:${Versions.compose}" }
    val composeMaterial by lazy { "androidx.compose.material:material:${Versions.compose}" }
    val composeAnimation by lazy { "androidx.compose.animation:animation:${Versions.compose}" }
    val composePreview by lazy { "androidx.compose.ui:ui-tooling-preview:${Versions.compose}" }
    val composeActivity by lazy { "androidx.activity:activity-compose:${Versions.activityCompose}" }
    val composeViewModel by lazy { "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleViewModelCompose}" }
    val composeNavigation by lazy { "androidx.navigation:navigation-compose:${Versions.composeNavigation}" }
    val composeMaterialIconsCore by lazy { "androidx.compose.material:material-icons-core:${Versions.compose}" }
    val composeMaterialIconsExtended by lazy { "androidx.compose.material:material-icons-extended:${Versions.compose}" }
    val composeFoundation by lazy { "androidx.compose.foundation:foundation:${Versions.compose}" }
    val composeFoundationLayout by lazy { "androidx.compose.foundation:foundation-layout:${Versions.compose}" }
    val composeConstraintLayout by lazy { "androidx.constraintlayout:constraintlayout-compose:${Versions.composeConstraintLayout}" }
    val composeMaterial3 by lazy { "androidx.compose.material3:material3:${Versions.composeMaterial3}" }

    // Networking & JSON
    val retrofit by lazy { "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}" }
    val moshi by lazy { "com.squareup.moshi:moshi:${Versions.moshi}" }
    val moshiKotlin by lazy { "com.squareup.moshi:moshi-kotlin:${Versions.moshi}" }

    // Timber Logging
    val timber by lazy { "com.jakewharton.timber:timber:${Versions.timber}" }

    // Dagger Hilt
    val hilt by lazy { "com.google.dagger:hilt-android:${Versions.hilt}" }
    val hiltNavigationCompose by lazy { "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationCompose}" }
    val hiltCompiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.hilt}" }
    // Hilt testing
    val hiltTest by lazy { "com.google.dagger:hilt-android-testing:${Versions.hilt}" }

    // Coil image loader
    val coilImage by lazy { "io.coil-kt:coil-compose:${Versions.coilImage}" }

    // Accompanist
    val accompanistInsets by lazy { "com.google.accompanist:accompanist-insets:${Versions.accompanist}" }
    val accompanistNavigationAnimations by lazy { "com.google.accompanist:accompanist-navigation-animation:${Versions.accompanist}" }

    // Room
    val roomRuntime by lazy { "androidx.room:room-runtime:${Versions.room}" }
    val roomCompiler by lazy { "androidx.room:room-compiler:${Versions.room}" }
    val roomKtx by lazy { "androidx.room:room-ktx:${Versions.room}" }
    val roomTest by lazy { "androidx.room:room-testing:${Versions.room}" }

    // Preferences DataStore
    val prefDataStore by lazy { "androidx.datastore:datastore-preferences:${Versions.dataStore}" }

    // Splash Screen
    val splashScreenCore by lazy { "androidx.core:core-splashscreen:${Versions.splashScreen}" }

}
package work.racka.reluct.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import work.racka.reluct.BuildConfig
import work.racka.reluct.android.navigation.util.AccountCheck
import work.racka.reluct.android.navigation.util.SettingsCheck
import work.racka.reluct.common.settings.MultiplatformSettings
import work.racka.reluct.compose.common.theme.Theme

class MainActivity : ComponentActivity() {

    private val settings: MultiplatformSettings by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable support for Splash Screen API for
        // proper Android 12+ support
        installSplashScreen()

        // Enable edge-to-edge experience and ProvideWindowInsets to the composable
        WindowCompat.setDecorFitsSystemWindows(window, false)

        triggerBackgroundChangeIfNeeded()

        val auth = Firebase.auth
        setContent {
            // Theming Stuff
            val themeValue by settings.theme.collectAsState(
                Theme.FOLLOW_SYSTEM.themeValue,
                Dispatchers.Main.immediate
            )
            val systemUiController = rememberSystemUiController()
            val isDarkMode = isSystemInDarkTheme()
            val useDarkIcons by remember(themeValue) {
                derivedStateOf {
                    when (themeValue) {
                        Theme.FOLLOW_SYSTEM.themeValue -> !isDarkMode
                        Theme.DARK_THEME.themeValue -> false
                        Theme.LIGHT_THEME.themeValue -> true
                        else -> !isDarkMode
                    }
                }
            }

            LaunchedEffect(useDarkIcons) {
                systemUiController.apply {
                    setStatusBarColor(
                        darkIcons = useDarkIcons,
                        color = Color.Transparent
                    )
                    setNavigationBarColor(
                        darkIcons = useDarkIcons,
                        color = Color.Transparent
                    )
                }
            }

            // Settings for determining start destinations
            val settingsCheck = produceState<SettingsCheck?>(initialValue = null) {
                value = getSettingsCheck(auth)
            }

            // Root Compose Entry
            ReluctMainCompose(themeValue = themeValue, settingsCheck = settingsCheck)
        }
    }

    /**
     * Provide Settings check
     */
    private suspend fun getSettingsCheck(auth: FirebaseAuth): SettingsCheck =
        withContext(Dispatchers.IO) {
            val accountCheck = auth.currentUser?.let { user ->
                AccountCheck(user.isEmailVerified, user.email ?: "NO_EMAIL")
            }
            val loginSkipped = settings.loginSkipped.firstOrNull()
            val onBoardingShown = settings.onBoardingShown.firstOrNull()
            val savedVersionCode = settings.savedVersionCode.firstOrNull()
                ?: (BuildConfig.VERSION_CODE)
            settings.saveVersionCode(BuildConfig.VERSION_CODE)
            SettingsCheck(
                isOnBoardingDone = onBoardingShown ?: false,
                showChangeLog = BuildConfig.VERSION_CODE > savedVersionCode,
                accountCheck = accountCheck,
                loginSkipped = loginSkipped ?: false
            )
        }

    /**
     * Function to tackle https://issuetracker.google.com/issues/227926002.
     * This will trigger the compose NavHost to load the startDestination without having a user input.
     */
    private fun triggerBackgroundChangeIfNeeded() {
        if (isMiui()) {
            lifecycleScope.launch {
                delay(400)
                window.setBackgroundDrawableResource(android.R.color.transparent)
            }
        }
    }

    /**
     * Function to determine if the device runs on MIUI by Xiaomi.
     */
    private fun isMiui(): Boolean {
        return try {
            Class
                .forName("android.os.SystemProperties")
                .getMethod("get", String::class.java)
                .let { propertyClass ->
                    propertyClass
                        .invoke(propertyClass, "ro.miui.ui.version.name")
                        ?.toString()
                        ?.isNotEmpty()
                        ?: false
                }
        } catch (e: Exception) {
            // e.printStackTrace()
            println("Property not found: ${e.message}")
            false
        }
    }
}

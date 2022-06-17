package work.racka.reluct.ui

import android.app.AppOpsManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import timber.log.Timber
import work.racka.reluct.android.compose.theme.Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable support for Splash Screen API for
        // proper Android 12+ support
        installSplashScreen()

        // Enable edge-to-edge experience and ProvideWindowInsets to the composable
        WindowCompat.setDecorFitsSystemWindows(window, false)


        AppOpsManager.OnOpChangedListener { op, packageName -> Timber.d("AppOp: $packageName") }

        setContent {
            val themeValue = Theme.MATERIAL_YOU.themeValue
            val systemUiController = rememberSystemUiController()
            val isDarkMode = isSystemInDarkTheme()
            val useDarkIcons = derivedStateOf {
                when (themeValue) {
                    Theme.FOLLOW_SYSTEM.themeValue -> !isDarkMode
                    Theme.DARK_THEME.themeValue -> false
                    Theme.LIGHT_THEME.themeValue -> true
                    else -> !isDarkMode
                }
            }
            SideEffect {
                systemUiController.setStatusBarColor(
                    darkIcons = useDarkIcons.value,
                    color = Color.Transparent
                )
            }
            ReluctMainCompose(themeValue = themeValue)
        }
    }
}

package work.racka.reluct.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ProvideWindowInsets
import timber.log.Timber
import work.racka.reluct.compose.navigation.navhost.AppNavHost
import work.racka.reluct.ui.theme.ReluctAppTheme

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun ReluctApp(
    themeValue: Int = -1
) {

    ProvideWindowInsets {
        ReluctAppTheme(theme = themeValue) {
            Timber.d("Reluct app called")
            AppNavHost()
        }
    }

}
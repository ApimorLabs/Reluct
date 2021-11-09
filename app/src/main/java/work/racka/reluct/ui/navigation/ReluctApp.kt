package work.racka.reluct.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import timber.log.Timber
import work.racka.reluct.ui.navigation.navhost.ReluctAppNavHost
import work.racka.reluct.ui.theme.ReluctAppTheme

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun ReluctApp(
    themeValue: Int = 12
) {

    ProvideWindowInsets {
        ReluctAppTheme(theme = themeValue) {
            Timber.d("rally app called")
            val navController = rememberAnimatedNavController()
            ReluctAppNavHost(
                navController = navController
            )
        }
    }

}
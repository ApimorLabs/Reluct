package work.racka.reluct.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import timber.log.Timber
import work.racka.reluct.android.compose.navigation.navhost.AppNavHost
import work.racka.reluct.android.compose.theme.ReluctAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReluctApp(
    themeValue: Int = 12
) {
    ReluctAppTheme(theme = themeValue) {
        Timber.d("Reluct app called")
        Scaffold {
            AppNavHost()
        }
    }

}
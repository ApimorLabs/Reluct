package work.racka.reluct.ui

import androidx.compose.runtime.Composable
import work.racka.reluct.android.compose.navigation.navhost.AppNavHost
import work.racka.reluct.android.compose.theme.ReluctAppTheme

@Composable
fun ReluctMainCompose(themeValue: Int) {
    ReluctAppTheme(theme = themeValue) {
        AppNavHost()
    }
}
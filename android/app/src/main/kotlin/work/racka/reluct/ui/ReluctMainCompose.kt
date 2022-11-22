package work.racka.reluct.ui

import androidx.compose.runtime.Composable
import work.racka.reluct.android.navigation.navhost.AppNavHost
import work.racka.reluct.android.navigation.util.SettingsCheck
import work.racka.reluct.compose.common.theme.ReluctAppTheme

@Composable
fun ReluctMainCompose(themeValue: Int, settingsCheck: SettingsCheck?) {
    ReluctAppTheme(theme = themeValue) {
        AppNavHost(settingsCheck = settingsCheck)
    }
}

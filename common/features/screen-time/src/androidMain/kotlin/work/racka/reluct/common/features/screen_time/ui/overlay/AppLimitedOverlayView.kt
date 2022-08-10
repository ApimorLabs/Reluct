package work.racka.reluct.common.features.screen_time.ui.overlay

import android.content.Context
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import work.racka.reluct.android.compose.theme.ReluctAppTheme
import work.racka.reluct.android.compose.theme.Theme
import work.racka.reluct.common.features.screen_time.statistics.AppScreenTimeStatsViewModel
import work.racka.reluct.common.settings.MultiplatformSettings

internal class AppLimitedOverlayView(
    private val context: Context,
    private val viewModel: AppScreenTimeStatsViewModel,
    private val exit: (View) -> Unit
) : KoinComponent {

    private val preferences: MultiplatformSettings by inject()


    fun getView() = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            val themeValue = Theme.MATERIAL_YOU.themeValue
            ReluctAppTheme(theme = themeValue) {
                // Put You Stuff Here
                AppLimitedOverlayUI(
                    viewModel = viewModel,
                    exit = { exit(this) }
                )
            }
        }
    }
}
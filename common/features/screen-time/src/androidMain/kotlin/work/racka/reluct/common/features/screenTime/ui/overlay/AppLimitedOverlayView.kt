package work.racka.reluct.common.features.screenTime.ui.overlay

import android.content.Context
import android.view.View
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.ViewCompositionStrategy
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import work.racka.reluct.android.compose.theme.ReluctAppTheme
import work.racka.reluct.android.compose.theme.Theme
import work.racka.reluct.common.features.screenTime.statistics.AppScreenTimeStatsViewModel
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
                // Put Your Stuff Here
                val composeHaptics = LocalHapticFeedback.current
                val focusModeOn by preferences.focusMode.collectAsState(initial = false)
                AppLimitedOverlayUI(
                    viewModel = viewModel,
                    focusModeOn = focusModeOn,
                    exit = {
                        composeHaptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        exit(this)
                    }
                )
            }
        }
    }
}

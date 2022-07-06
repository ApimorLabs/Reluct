package work.racka.reluct.android.screens.screentime.statistics

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.inject
import org.koin.androidx.compose.viewModel
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStats
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsEvents
import work.racka.reluct.common.features.screen_time.viewmodels.ScreenTimeStatsViewModel

@Composable
fun ScreenTimeStatisticsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAppUsageInfo: (packageName: String) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    val viewModel: ScreenTimeStatsViewModel by viewModel()
    val uiState by viewModel.host.uiState.collectAsState()
    val events by viewModel.host.events.collectAsState(initial = ScreenTimeStatsEvents.Nothing)

    val other: ScreenTimeStats by inject()

    val context = LocalContext.current

    LaunchedEffect(events) {
        handleEvents(
            events = events,
            context = context,
            scope = this,
            scaffoldState = scaffoldState,
            navigateToAppUsageInfo = { onNavigateToAppUsageInfo(it) }
        )
    }

    ScreenTimeStatisticsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        scaffoldState = scaffoldState,
        uiState = uiState,
        getUsageData = { viewModel.host.permissionCheck(it) },
        onSelectDay = { dayIsoNumber -> viewModel.host.selectDay(dayIsoNumber) },
        onUpdateWeekOffset = { offsetValue -> viewModel.host.updateWeekOffset(offsetValue) },
        onAppUsageInfoClick = { appInfo -> viewModel.host.navigateToAppInfo(appInfo.packageName) },
        onAppTimeLimitSettingsClicked = { }
    )
}

private fun handleEvents(
    events: ScreenTimeStatsEvents,
    context: Context,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navigateToAppUsageInfo: (packageName: String) -> Unit,
) {
    when (events) {
        is ScreenTimeStatsEvents.Navigation.NavigateToAppInfo -> {
            navigateToAppUsageInfo(events.packageName)
        }
        is ScreenTimeStatsEvents.Navigation.OpenAppTimerSettings -> {

        }

        else -> {}
    }
}
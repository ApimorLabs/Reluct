package work.racka.reluct.android.screens.screentime.statistics

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStatsViewModel
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsEvents

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ScreenTimeStatisticsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAppUsageInfo: (packageName: String) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    val viewModel: ScreenTimeStatsViewModel = getCommonViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle(initialValue = ScreenTimeStatsEvents.Nothing)

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
        getUsageData = { viewModel.permissionCheck(it) },
        onSelectDay = { dayIsoNumber -> viewModel.selectDay(dayIsoNumber) },
        onUpdateWeekOffset = { offsetValue -> viewModel.updateWeekOffset(offsetValue) },
        onAppUsageInfoClick = { appInfo -> viewModel.navigateToAppInfo(appInfo.packageName) },
        onAppTimeLimitSettingsClicked = { viewModel.selectAppTimeLimit(it) },
        onSaveAppTimeLimitSettings = { hours, minutes -> viewModel.saveTimeLimit(hours, minutes) }
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
        is ScreenTimeStatsEvents.TimeLimitChange -> {
            val message =
                context.getString(R.string.time_limit_change_arg, events.app.appInfo.appName)
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is ScreenTimeStatsEvents.Navigation.NavigateToAppInfo -> {
            navigateToAppUsageInfo(events.packageName)
        }
        is ScreenTimeStatsEvents.Navigation.OpenAppTimerSettings -> {

        }
        else -> {}
    }
}
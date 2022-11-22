package work.racka.reluct.android.screens.screentime.statistics

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.screenTime.statistics.ScreenTimeStatsViewModel
import work.racka.reluct.common.features.screenTime.statistics.states.ScreenTimeStatsEvents
import work.racka.reluct.compose.common.components.util.BarsVisibility

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ScreenTimeStatisticsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAppUsageInfo: (packageName: String) -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: ScreenTimeStatsViewModel = getCommonViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events =
        viewModel.events.collectAsStateWithLifecycle(initialValue = ScreenTimeStatsEvents.Nothing)

    val context = LocalContext.current
    HandleEvents(
        eventsState = events,
        context = context,
        snackbarState = snackbarState,
        navigateToAppUsageInfo = { onNavigateToAppUsageInfo(it) }
    )

    ScreenTimeStatisticsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        snackbarHostState = snackbarState,
        uiState = uiState,
        getUsageData = viewModel::permissionCheck,
        onSelectDay = viewModel::selectDay,
        onUpdateWeekOffset = viewModel::updateWeekOffset,
        onAppUsageInfoClick = { appInfo -> viewModel.navigateToAppInfo(appInfo.packageName) },
        onAppTimeLimitSettingsClicked = viewModel::selectAppTimeLimit,
        onSaveAppTimeLimitSettings = viewModel::saveTimeLimit
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<ScreenTimeStatsEvents>,
    context: Context,
    snackbarState: SnackbarHostState,
    navigateToAppUsageInfo: (packageName: String) -> Unit,
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is ScreenTimeStatsEvents.TimeLimitChange -> {
                val message =
                    context.getString(R.string.time_limit_change_arg, events.app.appInfo.appName)
                launch {
                    snackbarState.showSnackbar(
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
}

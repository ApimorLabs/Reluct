package work.racka.reluct.android.screens.screentime.statistics

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: ScreenTimeStatsViewModel = getCommonViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle(initialValue = ScreenTimeStatsEvents.Nothing)

    val context = LocalContext.current

    LaunchedEffect(events) {
        handleEvents(
            events = events,
            context = context,
            scope = this,
            snackbarState = snackbarState,
            navigateToAppUsageInfo = { onNavigateToAppUsageInfo(it) }
        )
    }

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

private fun handleEvents(
    events: ScreenTimeStatsEvents,
    context: Context,
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
    navigateToAppUsageInfo: (packageName: String) -> Unit,
) {
    when (events) {
        is ScreenTimeStatsEvents.TimeLimitChange -> {
            val message =
                context.getString(R.string.time_limit_change_arg, events.app.appInfo.appName)
            scope.launch {
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
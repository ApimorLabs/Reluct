package work.racka.reluct.android.screens.screentime.limits

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.compose.commonViewModel
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.common.features.screen_time.limits.ScreenTimeLimitsViewModel
import work.racka.reluct.common.features.screen_time.limits.states.ScreenTimeLimitsEvents

@Composable
fun ScreenTimeLimitsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAppUsageInfo: (packageName: String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    val viewModel: ScreenTimeLimitsViewModel by commonViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = ScreenTimeLimitsEvents.Nothing)

    LaunchedEffect(events) {
        handleEvents(
            events = events,
            scope = this,
            scaffoldState = scaffoldState,
            navigateToAppUsageInfo = { onNavigateToAppUsageInfo(it) }
        )
    }

    ScreenTimeLimitsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        scaffoldState = scaffoldState,
        uiState = uiState,
        toggleFocusMode = { viewModel.toggleFocusMode(it) },
        toggleDnd = { viewModel.toggleDnd(it) },
        getDistractingApps = { viewModel.getDistractingApps() },
        pauseApp = { viewModel.pauseApp(it) },
        resumeApp = { viewModel.unPauseApp(it) },
        makeDistractingApp = { viewModel.markAsDistracting(it) },
        removeDistractingApp = { viewModel.markAsNonDistracting(it) }
    )

}

private fun handleEvents(
    events: ScreenTimeLimitsEvents,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navigateToAppUsageInfo: (packageName: String) -> Unit,
) {
    when (events) {
        is ScreenTimeLimitsEvents.Navigation.NavigateToAppInfo -> {
            navigateToAppUsageInfo(events.packageName)
        }
        is ScreenTimeLimitsEvents.Navigation.OpenAppTimerSettings -> {

        }
        is ScreenTimeLimitsEvents.ShowMessageDone -> {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = events.msg,
                    duration = SnackbarDuration.Short
                )
            }
        }

        else -> {}
    }
}
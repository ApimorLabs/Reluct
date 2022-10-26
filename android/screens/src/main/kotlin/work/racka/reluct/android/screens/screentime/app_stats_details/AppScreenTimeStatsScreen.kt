package work.racka.reluct.android.screens.screentime.app_stats_details

import android.content.Context
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
import org.koin.core.parameter.parametersOf
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.screen_time.statistics.AppScreenTimeStatsViewModel
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsEvents

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AppScreenTimeStatsScreen(
    packageName: String,
    onBackClicked: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: AppScreenTimeStatsViewModel = getCommonViewModel { parametersOf(packageName) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle(initialValue = ScreenTimeStatsEvents.Nothing)

    val context = LocalContext.current
    LaunchedEffect(events) {
        handleEvents(
            context = context,
            events = events,
            scope = this,
            snackbarHostState = snackbarHostState
        )
    }

    AppScreenTimeStatsUI(
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        toggleDistractingState = viewModel::toggleDistractingState,
        togglePausedState = viewModel::togglePausedState,
        saveTimeLimit = viewModel::saveTimeLimit,
        onSelectDay = viewModel::selectDay,
        onUpdateWeekOffset = viewModel::updateWeekOffset,
        goBack = onBackClicked
    )
}

private fun handleEvents(
    events: ScreenTimeStatsEvents,
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    when (events) {
        is ScreenTimeStatsEvents.ShowMessageDone -> {
            val message = events.msg
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is ScreenTimeStatsEvents.TimeLimitChange -> {
            val message =
                context.getString(R.string.time_limit_change_arg, events.app.appInfo.appName)
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }
        else -> {}
    }
}
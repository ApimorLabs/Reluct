package work.racka.reluct.android.screens.screentime.app_stats_details

import android.content.Context
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import work.racka.common.mvvm.koin.compose.commonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.screen_time.statistics.AppScreenTimeStatsViewModel
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsEvents

@Composable
fun AppScreenTimeStatsScreen(
    packageName: String,
    onBackClicked: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    val viewModel: AppScreenTimeStatsViewModel by commonViewModel { parametersOf(packageName) }
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = ScreenTimeStatsEvents.Nothing)

    val context = LocalContext.current
    LaunchedEffect(events) {
        handleEvents(
            context = context,
            events = events,
            scope = this,
            scaffoldState = scaffoldState
        )
    }

    AppScreenTimeStatsUI(
        scaffoldState = scaffoldState,
        uiState = uiState,
        toggleDistractingState = { viewModel.toggleDistractingState(it) },
        saveTimeLimit = { hours, minutes -> viewModel.saveTimeLimit(hours, minutes) },
        onSelectDay = { viewModel.selectDay(it) },
        onUpdateWeekOffset = { viewModel.updateWeekOffset(it) },
        goBack = onBackClicked
    )
}

private fun handleEvents(
    events: ScreenTimeStatsEvents,
    context: Context,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    when (events) {
        is ScreenTimeStatsEvents.ShowMessageDone -> {
            val message = events.msg
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }
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
        else -> {}
    }
}
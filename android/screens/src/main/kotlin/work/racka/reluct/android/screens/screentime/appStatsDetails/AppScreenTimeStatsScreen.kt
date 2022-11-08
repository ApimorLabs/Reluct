package work.racka.reluct.android.screens.screentime.appStatsDetails

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.screenTime.statistics.AppScreenTimeStatsViewModel
import work.racka.reluct.common.features.screenTime.statistics.states.ScreenTimeStatsEvents

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AppScreenTimeStatsScreen(
    packageName: String,
    onBackClicked: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: AppScreenTimeStatsViewModel = getCommonViewModel { parametersOf(packageName) }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events =
        viewModel.events.collectAsStateWithLifecycle(initialValue = ScreenTimeStatsEvents.Nothing)

    val context = LocalContext.current
    HandleEvents(
        context = context,
        eventsState = events,
        snackbarHostState = snackbarHostState
    )

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

@Composable
private fun HandleEvents(
    eventsState: State<ScreenTimeStatsEvents>,
    context: Context,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is ScreenTimeStatsEvents.ShowMessageDone -> {
                val message = events.msg
                launch {
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is ScreenTimeStatsEvents.TimeLimitChange -> {
                val message =
                    context.getString(R.string.time_limit_change_arg, events.app.appInfo.appName)
                launch {
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            else -> {}
        }
    }
}

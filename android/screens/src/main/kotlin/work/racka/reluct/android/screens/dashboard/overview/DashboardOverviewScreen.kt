package work.racka.reluct.android.screens.dashboard.overview

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
import work.racka.reluct.common.features.dashboard.overview.DashboardOverviewViewModel
import work.racka.reluct.common.features.dashboard.overview.states.DashboardEvents
import work.racka.reluct.compose.common.components.util.BarsVisibility

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun DashboardOverviewScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToScreenTime: () -> Unit,
    onNavigateToTaskDetails: (taskId: String) -> Unit,
    onNavigateToGoalDetails: (goalId: String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: DashboardOverviewViewModel = getCommonViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events =
        viewModel.events.collectAsStateWithLifecycle(initialValue = DashboardEvents.Nothing)

    val context = LocalContext.current
    HandleEvents(
        context = context,
        eventsState = events,
        snackbarHostState = snackbarHostState
    )

    DashboardOverviewUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        getUsageData = { viewModel.permissionCheck(isGranted = it) },
        openScreenTimeStats = onNavigateToScreenTime,
        openPendingTask = { onNavigateToTaskDetails(it.id) },
        onToggleTaskDone = { isDone, task -> viewModel.toggleDone(task = task, isDone = isDone) },
        onGoalClicked = { onNavigateToGoalDetails(it.id) }
    )
}

@Composable
private fun HandleEvents(
    context: Context,
    eventsState: State<DashboardEvents>,
    snackbarHostState: SnackbarHostState,
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is DashboardEvents.ShowMessageDone -> {
                launch {
                    val msg = if (events.isDone) {
                        context.getString(R.string.task_marked_as_done, events.msg)
                    } else {
                        context.getString(R.string.task_marked_as_not_done, events.msg)
                    }
                    snackbarHostState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            else -> {}
        }
    }
}

package work.racka.reluct.android.screens.dashboard.overview

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
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
import work.racka.common.mvvm.koin.compose.commonViewModel
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.dashboard.overview.DashboardOverviewViewModel
import work.racka.reluct.common.features.dashboard.overview.states.DashboardEvents

@Composable
fun DashboardOverviewScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToScreenTime: () -> Unit,
    onNavigateToTaskDetails: (taskId: String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    val viewModel: DashboardOverviewViewModel by commonViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = DashboardEvents.Nothing)

    val context = LocalContext.current
    LaunchedEffect(events) {
        handleEvents(
            context = context,
            scope = this,
            events = events,
            scaffoldState = scaffoldState
        )
    }

    DashboardOverviewUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        scaffoldState = scaffoldState,
        uiState = uiState,
        getUsageData = { viewModel.permissionCheck(isGranted = it) },
        openScreenTimeStats = onNavigateToScreenTime,
        openPendingTask = { onNavigateToTaskDetails(it.id) },
        onToggleTaskDone = { isDone, task -> viewModel.toggleDone(task = task, isDone = isDone) }
    )
}

private fun handleEvents(
    context: Context,
    events: DashboardEvents,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    when (events) {
        is DashboardEvents.ShowMessageDone -> {
            scope.launch {
                val msg = if (events.isDone) {
                    context.getString(R.string.task_marked_as_done, events.msg)
                } else context.getString(R.string.task_marked_as_not_done, events.msg)
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        else -> {}
    }
}
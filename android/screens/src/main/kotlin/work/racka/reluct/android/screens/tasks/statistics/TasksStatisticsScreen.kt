package work.racka.reluct.android.screens.tasks.statistics

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
import work.racka.reluct.common.features.tasks.statistics.TasksStatisticsViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents

@Composable
fun TasksStatisticsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToTaskDetails: (taskId: String) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    val viewModel: TasksStatisticsViewModel by commonViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = TasksEvents.Nothing)

    val context = LocalContext.current

    LaunchedEffect(events) {
        handleEvents(
            context = context,
            events = events,
            scope = this,
            scaffoldState = scaffoldState,
            navigateToTaskDetails = { taskId ->
                onNavigateToTaskDetails(taskId)
            }
        )
    }

    TasksStatisticsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        scaffoldState = scaffoldState,
        uiState = uiState,
        onSelectDay = { dayIsoNumber -> viewModel.selectDay(dayIsoNumber) },
        onTaskClicked = { onNavigateToTaskDetails(it.id) },
        onToggleTaskDone = { isDone, task ->
            viewModel.toggleDone(task, isDone)
        },
        onUpdateWeekOffset = { viewModel.updateWeekOffset(it) }
    )
}

private fun handleEvents(
    context: Context,
    events: TasksEvents,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navigateToTaskDetails: (taskId: String) -> Unit,
) {
    when (events) {
        is TasksEvents.ShowMessageDone -> {
            val msg = if (events.isDone) context.getString(R.string.task_marked_as_done, events.msg)
            else context.getString(R.string.task_marked_as_not_done, events.msg)
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is TasksEvents.Navigation.NavigateToTaskDetails -> {
            navigateToTaskDetails(events.taskId)
        }
        else -> {}
    }
}
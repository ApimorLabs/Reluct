package work.racka.reluct.android.screens.tasks.pending

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.tasks.viewmodels.PendingTasksViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents

@Composable
fun PendingTasksScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAddTask: (taskId: String?) -> Unit,
    onNavigateToTaskDetails: (taskId: String) -> Unit,
) {

    val scaffoldState = rememberScaffoldState()

    val viewModel: PendingTasksViewModel by viewModel()
    val uiState by viewModel.host.uiState.collectAsState()
    val events by viewModel.host.events.collectAsState(initial = TasksEvents.Nothing)

    val taskDone = stringResource(R.string.task_marked_as_done)

    LaunchedEffect(events) {
        handleEvents(
            events = events,
            scope = this,
            scaffoldState = scaffoldState,
            taskDoneText = taskDone,
            navigateToTaskDetails = { taskId ->
                onNavigateToTaskDetails(taskId)
            }
        )
    }

    PendingTasksUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        uiState = uiState,
        scaffoldState = scaffoldState,
        onTaskClicked = {
            viewModel.host.navigateToTaskDetails(it.id)
        },
        onAddTaskClicked = {
            onNavigateToAddTask(it?.id)
        },
        onToggleTaskDone = { isDone, task ->
            viewModel.host.toggleDone(task, isDone)
        },
        fetchMoreData = { viewModel.host.fetchMoreData() }
    )
}

private fun handleEvents(
    events: TasksEvents,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    taskDoneText: String,
    navigateToTaskDetails: (taskId: String) -> Unit,
) {
    when (events) {
        is TasksEvents.ShowMessageDone -> {
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = taskDoneText,
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
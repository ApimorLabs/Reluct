package work.racka.reluct.android.screens.tasks.pending

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
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.tasks.viewmodels.PendingTasksViewModel
import work.racka.reluct.common.model.states.tasks.TasksSideEffect

@Composable
fun PendingTasksScreen(
    onNavigateToAddTask: (taskId: String?) -> Unit,
    onNavigateToTaskDetails: (taskId: String) -> Unit,
) {

    val scaffoldState = rememberScaffoldState()

    val viewModel: PendingTasksViewModel by viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = TasksSideEffect.Nothing)

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
        uiState = uiState,
        scaffoldState = scaffoldState,
        onTaskClicked = {
            viewModel.navigateToTaskDetails(it.id)
        },
        onAddTaskClicked = {
            onNavigateToAddTask(it?.id)
        },
        onToggleTaskDone = { isDone, task ->
            viewModel.toggleDone(task, isDone)
        }
    )
}

private fun handleEvents(
    events: TasksSideEffect,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    taskDoneText: String,
    navigateToTaskDetails: (taskId: String) -> Unit,
) {
    when (events) {
        is TasksSideEffect.ShowMessageDone -> {
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = taskDoneText,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is TasksSideEffect.Navigation.NavigateToTaskDetails -> {
            navigateToTaskDetails(events.taskId)
        }
        else -> {}
    }
}
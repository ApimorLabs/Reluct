package work.racka.reluct.android.screens.tasks.search

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
import work.racka.reluct.common.features.tasks.viewmodels.SearchTasksViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents

@Composable
fun TasksSearchScreen(
    onNavigateToTaskDetails: (taskId: String) -> Unit,
    onBackClicked: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    val viewModel: SearchTasksViewModel by viewModel()
    val uiState by viewModel.host.uiState.collectAsState()
    val events by viewModel.host.events.collectAsState(initial = TasksEvents.Nothing)

    val taskDone = stringResource(R.string.task_marked_as_done)
    val taskNotDone = stringResource(R.string.task_marked_as_not_done)

    LaunchedEffect(events) {
        handleEvents(
            events = events,
            scope = this,
            scaffoldState = scaffoldState,
            taskDoneText = taskDone,
            taskNotDoneText = taskNotDone,
            navigateToTaskDetails = { taskId -> onNavigateToTaskDetails(taskId) },
            goBack = onBackClicked
        )
    }

    TasksSearchUI(scaffoldState = scaffoldState,
        uiState = uiState,
        onSearch = { viewModel.host.search(it) },
        onTaskClicked = { viewModel.host.navigateToTaskDetails(it.id) },
        onToggleTaskDone = { isDone, task ->
            viewModel.host.toggleDone(task, isDone)
        }
    )
}

private fun handleEvents(
    events: TasksEvents,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    taskDoneText: String,
    taskNotDoneText: String,
    navigateToTaskDetails: (taskId: String) -> Unit,
    goBack: () -> Unit,
) {
    when (events) {
        is TasksEvents.ShowMessage -> {
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = events.msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is TasksEvents.ShowMessageDone -> {
            val msg = if (events.isDone) taskDoneText else taskNotDoneText
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is TasksEvents.Navigation.NavigateToTaskDetails -> navigateToTaskDetails(events.taskId)
        is TasksEvents.Navigation.GoBack -> goBack()
        else -> {}
    }
}
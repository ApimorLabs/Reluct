package work.racka.reluct.android.screens.tasks.details

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
import org.koin.core.parameter.parametersOf
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.tasks.viewmodels.TaskDetailsViewModel
import work.racka.reluct.common.model.states.tasks.TasksSideEffect

@Composable
fun TaskDetailsScreen(
    taskId: String?,
    onNavigateToEditTask: (taskId: String) -> Unit,
    onBackClicked: () -> Unit,
) {

    val scaffoldState = rememberScaffoldState()

    val viewModel: TaskDetailsViewModel by viewModel { parametersOf(taskId) }
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = TasksSideEffect.Nothing)

    val taskDone = stringResource(R.string.task_marked_as_done)
    val taskNotDone = stringResource(R.string.task_marked_as_not_done)

    LaunchedEffect(events) {
        handleEvents(
            events = events,
            scope = this,
            scaffoldState = scaffoldState,
            taskDoneText = taskDone,
            taskNotDoneText = taskNotDone,
            navigateToEditTask = { taskId -> onNavigateToEditTask(taskId) },
            goBack = onBackClicked
        )
    }

    TaskDetailsUI(
        uiState = uiState,
        scaffoldState = scaffoldState,
        onEditTask = { task ->
            task?.let { viewModel.editTask(it.id) }
        },
        onDeleteTask = { task ->
            task?.let { viewModel.deleteTask(it.id) }
            onBackClicked()
        },
        onToggleTaskDone = { isDone, task ->
            viewModel.toggleDone(task, isDone)
        },
        onBackClicked = onBackClicked
    )
}

private fun handleEvents(
    events: TasksSideEffect,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    taskDoneText: String,
    taskNotDoneText: String,
    navigateToEditTask: (taskId: String) -> Unit,
    goBack: () -> Unit,
) {
    when (events) {
        is TasksSideEffect.ShowMessage -> {
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = events.msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is TasksSideEffect.ShowMessageDone -> {
            val msg = if (events.isDone) taskDoneText else taskNotDoneText
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is TasksSideEffect.Navigation.NavigateToEdit -> navigateToEditTask(events.taskId)
        is TasksSideEffect.Navigation.GoBack -> goBack()
        else -> {}
    }
}
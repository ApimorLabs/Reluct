package work.racka.reluct.android.screens.tasks.details

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
import work.racka.reluct.common.features.tasks.task_details.TaskDetailsViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents

@Composable
fun TaskDetailsScreen(
    taskId: String?,
    onNavigateToEditTask: (taskId: String) -> Unit,
    onBackClicked: () -> Unit,
) {

    val scaffoldState = rememberScaffoldState()

    val viewModel: TaskDetailsViewModel by commonViewModel { parametersOf(taskId) }
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = TasksEvents.Nothing)

    val context = LocalContext.current

    LaunchedEffect(events) {
        handleEvents(
            context = context,
            events = events,
            scope = this,
            scaffoldState = scaffoldState,
            navigateToEditTask = { taskId -> onNavigateToEditTask(taskId) },
            goBack = onBackClicked
        )
    }

    TaskDetailsUI(
        uiState = uiState,
        scaffoldState = scaffoldState,
        onEditTask = { viewModel.editTask(it.id) },
        onDeleteTask = { viewModel.deleteTask(it.id) },
        onToggleTaskDone = { isDone, task -> viewModel.toggleDone(task, isDone) },
        onBackClicked = { viewModel.goBack() }
    )
}

private fun handleEvents(
    context: Context,
    events: TasksEvents,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navigateToEditTask: (taskId: String) -> Unit,
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
            val msg = if (events.isDone) context.getString(R.string.task_marked_as_done, events.msg)
            else context.getString(R.string.task_marked_as_not_done, events.msg)
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is TasksEvents.Navigation.NavigateToEdit -> navigateToEditTask(events.taskId)
        is TasksEvents.Navigation.GoBack -> goBack()
        else -> {}
    }
}
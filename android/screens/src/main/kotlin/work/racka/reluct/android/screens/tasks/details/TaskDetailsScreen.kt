package work.racka.reluct.android.screens.tasks.details

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.tasks.components.ModifyTaskLabel
import work.racka.reluct.common.features.tasks.task_details.TaskDetailsViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun TaskDetailsScreen(
    taskId: String?,
    onNavigateToEditTask: (taskId: String) -> Unit,
    onBackClicked: () -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: TaskDetailsViewModel = getCommonViewModel { parametersOf(taskId) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle(initialValue = TasksEvents.Nothing)

    val context = LocalContext.current

    LaunchedEffect(events) {
        handleEvents(
            context = context,
            events = events,
            scope = this,
            snackbarState = snackbarState,
            navigateToEditTask = { taskId -> onNavigateToEditTask(taskId) },
            goBack = onBackClicked
        )
    }

    TaskDetailsUI(
        uiState = uiState,
        snackbarState = snackbarState,
        onEditTask = { viewModel.editTask(it.id) },
        onDeleteTask = { viewModel.deleteTask(it.id) },
        onToggleTaskDone = { isDone, task -> viewModel.toggleDone(task, isDone) },
        onBackClicked = { viewModel.goBack() },
        onModifyTaskLabel = { modifyLabel ->
            when (modifyLabel) {
                is ModifyTaskLabel.SaveLabel -> modifyLabel.label.run(viewModel::saveLabel)
                is ModifyTaskLabel.Delete -> modifyLabel.label.run(viewModel::deleteLabel)
            }
        }
    )
}

private fun handleEvents(
    context: Context,
    events: TasksEvents,
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
    navigateToEditTask: (taskId: String) -> Unit,
    goBack: () -> Unit,
) {
    when (events) {
        is TasksEvents.ShowMessage -> {
            scope.launch {
                snackbarState.showSnackbar(
                    message = events.msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is TasksEvents.ShowMessageDone -> {
            val msg = if (events.isDone) {
                context.getString(R.string.task_marked_as_done, events.msg)
            } else {
                context.getString(R.string.task_marked_as_not_done, events.msg)
            }
            scope.launch {
                snackbarState.showSnackbar(
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

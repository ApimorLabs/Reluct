package work.racka.reluct.android.screens.tasks.details

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
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = TasksEvents.Nothing)

    val context = LocalContext.current

    HandleEvents(
        context = context,
        eventsState = events,
        snackbarState = snackbarState,
        navigateToEditTask = { id -> onNavigateToEditTask(id) },
        goBack = onBackClicked
    )

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

@Composable
private fun HandleEvents(
    context: Context,
    eventsState: State<TasksEvents>,
    snackbarState: SnackbarHostState,
    navigateToEditTask: (taskId: String) -> Unit,
    goBack: () -> Unit,
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is TasksEvents.ShowMessage -> {
                launch {
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
                launch {
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
}

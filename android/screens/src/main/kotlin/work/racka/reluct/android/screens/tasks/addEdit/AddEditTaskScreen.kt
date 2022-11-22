package work.racka.reluct.android.screens.tasks.addEdit

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.tasks.components.ModifyTaskLabel
import work.racka.reluct.common.features.tasks.addEditTask.AddEditTaskViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AddEditTaskScreen(
    taskId: String?,
    onBackClicked: () -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }
    val viewModel: AddEditTaskViewModel = getCommonViewModel { parametersOf(taskId) }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(TasksEvents.Nothing)

    HandleEvents(
        eventsState = events,
        snackbarState = snackbarState,
        goBack = onBackClicked
    )

    AddEditTaskUI(
        snackbarState = snackbarState,
        uiState = uiState,
        onSaveTask = viewModel::saveCurrentTask,
        onAddTaskClicked = viewModel::newTask,
        onUpdateTask = viewModel::updateCurrentTask,
        onBackClicked = onBackClicked,
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
    eventsState: State<TasksEvents>,
    snackbarState: SnackbarHostState,
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
            is TasksEvents.Navigation.GoBack -> goBack()
            else -> {}
        }
    }
}

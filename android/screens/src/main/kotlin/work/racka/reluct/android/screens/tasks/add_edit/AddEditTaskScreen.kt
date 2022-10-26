package work.racka.reluct.android.screens.tasks.add_edit

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTaskViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AddEditTaskScreen(
    taskId: String?,
    onBackClicked: () -> Unit,
) {

    val snackbarState = remember { SnackbarHostState() }
    val viewModel: AddEditTaskViewModel = getCommonViewModel { parametersOf(taskId) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle(TasksEvents.Nothing)

    LaunchedEffect(events) {
        Timber.d("Event is : $events")
        handleEvents(
            events = events,
            scope = this,
            snackbarState = snackbarState,
            goBack = onBackClicked
        )
    }

    AddEditTaskUI(
        snackbarState = snackbarState,
        uiState = uiState,
        onSaveTask = viewModel::saveCurrentTask,
        onAddTaskClicked = viewModel::newTask,
        onUpdateTask = viewModel::updateCurrentTask,
        onBackClicked = onBackClicked
    )
}

private fun handleEvents(
    events: TasksEvents,
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
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
        is TasksEvents.Navigation.GoBack -> goBack()
        else -> {}
    }
}
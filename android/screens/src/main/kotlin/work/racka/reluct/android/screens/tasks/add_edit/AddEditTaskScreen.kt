package work.racka.reluct.android.screens.tasks.add_edit

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTaskViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents

@Composable
fun AddEditTaskScreen(
    taskId: String?,
    onBackClicked: () -> Unit,
) {

    val scaffoldState = rememberScaffoldState()
    val viewModel: AddEditTaskViewModel = getCommonViewModel { parametersOf(taskId) }
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(TasksEvents.Nothing)

    LaunchedEffect(events) {
        Timber.d("Event is : $events")
        handleEvents(
            events = events,
            scope = this,
            scaffoldState = scaffoldState,
            goBack = onBackClicked
        )
    }

    AddEditTaskUI(
        scaffoldState = scaffoldState,
        uiState = uiState,
        onSaveTask = { viewModel.saveTask(it) },
        onAddTaskClicked = { viewModel.getTask(null) },
        onUpdateTask = {},
        onBackClicked = { viewModel.goBack() }
    )
}

private fun handleEvents(
    events: TasksEvents,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
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
        is TasksEvents.Navigation.GoBack -> goBack()
        else -> {}
    }
}
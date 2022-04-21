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
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import work.racka.reluct.common.features.tasks.viewmodels.AddEditTaskViewModel
import work.racka.reluct.common.model.states.tasks.TasksSideEffect

@Composable
fun AddEditTaskScreen(
    taskId: String?,
    onBackClicked: () -> Unit,
) {

    val scaffoldState = rememberScaffoldState()
    val viewModel: AddEditTaskViewModel by viewModel {
        parametersOf(taskId)
    }
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(TasksSideEffect.Nothing)

    LaunchedEffect(events) {
        Timber.d("Launch Effect Called")
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
        onBackClicked = { viewModel.goBack() }
    )
}

private fun handleEvents(
    events: TasksSideEffect,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
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
        is TasksSideEffect.Navigation.GoBack -> {
            goBack()
        }
        else -> {}
    }
}
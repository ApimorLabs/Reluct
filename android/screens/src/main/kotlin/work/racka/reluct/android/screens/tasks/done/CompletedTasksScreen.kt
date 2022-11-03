package work.racka.reluct.android.screens.tasks.done

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
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
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasksViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun CompletedTasksScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAddTask: (taskId: String?) -> Unit,
    onNavigateToTaskDetails: (taskId: String) -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: CompletedTasksViewModel = getCommonViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle(initialValue = TasksEvents.Nothing)

    val context = LocalContext.current

    LaunchedEffect(events) {
        handleEvents(
            events = events,
            context = context,
            scope = this,
            snackbarState = snackbarState,
            navigateToTaskDetails = { taskId ->
                onNavigateToTaskDetails(taskId)
            }
        )
    }

    CompletedTasksUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        uiState = uiState,
        snackbarState = snackbarState,
        onTaskClicked = { viewModel.navigateToTaskDetails(it.id) },
        onAddTaskClicked = { onNavigateToAddTask(it?.id) },
        onToggleTaskDone = viewModel::toggleDone,
        fetchMoreData = viewModel::fetchMoreData
    )
}

private fun handleEvents(
    context: Context,
    events: TasksEvents,
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
    navigateToTaskDetails: (taskId: String) -> Unit,
) {
    when (events) {
        is TasksEvents.ShowMessageDone -> {
            scope.launch {
                snackbarState.showSnackbar(
                    message = context.getString(R.string.task_marked_as_not_done, events.msg),
                    duration = SnackbarDuration.Short
                )
            }
        }
        is TasksEvents.Navigation.NavigateToTaskDetails -> {
            navigateToTaskDetails(events.taskId)
        }
        else -> {}
    }
}

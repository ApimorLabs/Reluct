package work.racka.reluct.android.screens.tasks.done

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasksViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents
import work.racka.reluct.compose.common.components.util.BarsVisibility

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
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = TasksEvents.Nothing)

    val context = LocalContext.current
    HandleEvents(
        eventsState = events,
        context = context,
        snackbarState = snackbarState,
        navigateToTaskDetails = { taskId ->
            onNavigateToTaskDetails(taskId)
        }
    )

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

@Composable
private fun HandleEvents(
    context: Context,
    eventsState: State<TasksEvents>,
    snackbarState: SnackbarHostState,
    navigateToTaskDetails: (taskId: String) -> Unit,
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is TasksEvents.ShowMessageDone -> {
                launch {
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
}

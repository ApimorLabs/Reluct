package work.racka.reluct.android.screens.tasks.done

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
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
import work.racka.common.mvvm.koin.compose.commonViewModel
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasksViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents

@Composable
fun CompletedTasksScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAddTask: (taskId: String?) -> Unit,
    onNavigateToTaskDetails: (taskId: String) -> Unit,
) {

    val scaffoldState = rememberScaffoldState()

    val viewModel: CompletedTasksViewModel by commonViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = TasksEvents.Nothing)

    val context = LocalContext.current

    LaunchedEffect(events) {
        handleEvents(
            events = events,
            context = context,
            scope = this,
            scaffoldState = scaffoldState,
            navigateToTaskDetails = { taskId ->
                onNavigateToTaskDetails(taskId)
            }
        )
    }

    CompletedTasksUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        uiState = uiState,
        scaffoldState = scaffoldState,
        onTaskClicked = {
            viewModel.navigateToTaskDetails(it.id)
        },
        onAddTaskClicked = {
            onNavigateToAddTask(it?.id)
        },
        onToggleTaskDone = { isDone, task ->
            viewModel.toggleDone(task, isDone)
        },
        fetchMoreData = { viewModel.fetchMoreData() }
    )
}

private fun handleEvents(
    context: Context,
    events: TasksEvents,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navigateToTaskDetails: (taskId: String) -> Unit,
) {
    when (events) {
        is TasksEvents.ShowMessageDone -> {
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
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
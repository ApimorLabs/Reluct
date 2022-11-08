package work.racka.reluct.android.screens.tasks.statistics

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
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.tasks.statistics.TasksStatisticsViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun TasksStatisticsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToTaskDetails: (taskId: String) -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: TasksStatisticsViewModel = getCommonViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = TasksEvents.Nothing)

    val context = LocalContext.current

    HandleEvents(
        context = context,
        eventsState = events,
        snackbarState = snackbarState,
        navigateToTaskDetails = { taskId ->
            onNavigateToTaskDetails(taskId)
        }
    )

    TasksStatisticsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        snackbarState = snackbarState,
        uiState = uiState,
        onSelectDay = viewModel::selectDay,
        onTaskClicked = { onNavigateToTaskDetails(it.id) },
        onToggleTaskDone = viewModel::toggleDone,
        onUpdateWeekOffset = viewModel::updateWeekOffset
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
            is TasksEvents.Navigation.NavigateToTaskDetails -> {
                navigateToTaskDetails(events.taskId)
            }
            else -> {}
        }
    }
}

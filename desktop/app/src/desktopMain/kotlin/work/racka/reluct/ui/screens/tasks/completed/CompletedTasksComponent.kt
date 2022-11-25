package work.racka.reluct.ui.screens.tasks.completed

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.decompose.commonViewModel
import work.racka.reluct.common.features.tasks.completedTasks.CompletedTasksViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.ui.screens.ComposeRenderer

class CompletedTasksComponent(
    componentContext: ComponentContext,
    private val onShowDetails: (taskId: String) -> Unit,
    private val onAddTask: () -> Unit,
) : ComponentContext by componentContext, ComposeRenderer {

    private val viewModel by commonViewModel<CompletedTasksViewModel>()

    @Composable
    override fun Render(modifier: Modifier) {
        val snackbarState = remember { SnackbarHostState() }
        val uiState = viewModel.uiState.collectAsState()
        val events = viewModel.events.collectAsState(initial = TasksEvents.Nothing)

        HandleEvents(eventsState = events, snackbarState = snackbarState)

        CompletedTasksUI(
            modifier = modifier,
            uiState = uiState,
            snackbarState = snackbarState,
            onTaskClicked = { onShowDetails(it.id) },
            onAddTaskClicked = { onAddTask() },
            onToggleTaskDone = viewModel::toggleDone,
            fetchMoreData = viewModel::fetchMoreData
        )
    }

    @Composable
    private fun HandleEvents(
        eventsState: State<TasksEvents>,
        snackbarState: SnackbarHostState
    ) {
        LaunchedEffect(eventsState.value) {
            when (val events = eventsState.value) {
                is TasksEvents.ShowMessageDone -> {
                    launch {
                        snackbarState.showSnackbar(
                            message = StringDesc.ResourceFormatted(
                                SharedRes.strings.task_marked_as_not_done,
                                events.msg
                            ).localized(),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

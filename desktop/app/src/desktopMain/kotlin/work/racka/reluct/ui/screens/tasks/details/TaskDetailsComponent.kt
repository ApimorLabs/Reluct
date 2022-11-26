package work.racka.reluct.ui.screens.tasks.details

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import work.racka.common.mvvm.koin.decompose.commonViewModel
import work.racka.reluct.common.features.tasks.taskDetails.TaskDetailsViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.ui.screens.ComposeRenderer
import work.racka.reluct.ui.screens.tasks.components.ModifyTaskLabel

class TaskDetailsComponent(
    componentContext: ComponentContext,
    private val taskId: String?,
    private val onEdit: (taskId: String?) -> Unit,
    private val onClose: () -> Unit
) : ComponentContext by componentContext, ComposeRenderer {

    private val viewModel by commonViewModel<TaskDetailsViewModel> { parametersOf(taskId) }

    @Composable
    override fun Render(modifier: Modifier) {
        val snackbarState = remember { SnackbarHostState() }
        val uiState = viewModel.uiState.collectAsState()
        val events = viewModel.events.collectAsState(initial = TasksEvents.Nothing)

        HandleEvents(
            eventsState = events,
            snackbarState = snackbarState,
            goBack = onClose
        )

        TaskDetailsUI(
            modifier = modifier,
            uiState = uiState,
            snackbarState = snackbarState,
            onEditTask = { onEdit(it.id) },
            onDeleteTask = { viewModel.deleteTask(it.id) },
            onToggleTaskDone = { isDone, task -> viewModel.toggleDone(task, isDone) },
            onBackClicked = onClose,
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
                is TasksEvents.ShowMessageDone -> {
                    val msg = if (events.isDone) {
                        StringDesc.ResourceFormatted(
                            SharedRes.strings.task_marked_as_done,
                            events.msg
                        ).localized()
                    } else {
                        StringDesc.ResourceFormatted(
                            SharedRes.strings.task_marked_as_not_done,
                            events.msg
                        ).localized()
                    }
                    launch {
                        snackbarState.showSnackbar(
                            message = msg,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                is TasksEvents.Navigation.GoBack -> goBack()
                else -> {}
            }
        }
    }
}

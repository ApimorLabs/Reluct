package work.racka.reluct.ui.screens.tasks.addEdit

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import work.racka.common.mvvm.koin.decompose.commonViewModel
import work.racka.reluct.common.features.tasks.addEditTask.AddEditTaskViewModel
import work.racka.reluct.common.model.states.tasks.TasksEvents
import work.racka.reluct.ui.screens.ComposeRenderer
import work.racka.reluct.ui.screens.tasks.components.ModifyTaskLabel

class AddEditTaskComponent(
    componentContext: ComponentContext,
    private val taskId: String?,
    private val onClose: () -> Unit
) : ComponentContext by componentContext, ComposeRenderer {

    private val viewModel by commonViewModel<AddEditTaskViewModel> { parametersOf(taskId) }

    @Composable
    override fun Render(modifier: Modifier) {
        val snackbarState = remember { SnackbarHostState() }
        val uiState = viewModel.uiState.collectAsState()
        val events = viewModel.events.collectAsState(TasksEvents.Nothing)

        HandleEvents(
            eventsState = events,
            snackbarState = snackbarState,
            goBack = onClose
        )

        AddEditTaskUI(
            snackbarState = snackbarState,
            uiState = uiState,
            onSaveTask = viewModel::saveCurrentTask,
            onAddTaskClicked = viewModel::newTask,
            onUpdateTask = viewModel::updateCurrentTask,
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
                is TasksEvents.Navigation.GoBack -> goBack()
                else -> {}
            }
        }
    }
}

package work.racka.reluct.common.features.tasks.add_edit_task.container

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.add_edit_task.repository.AddEditTaskRepository
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

class AddEditTaskImpl(
    private val addEditTask: AddEditTaskRepository,
    private val taskId: Long?,
    private val scope: CoroutineScope
) : AddEditTask {
    private val _uiState: MutableStateFlow<TasksState> = MutableStateFlow(TasksState.Loading)
    private val _events: MutableStateFlow<TasksSideEffect> =
        MutableStateFlow(TasksSideEffect.Nothing)

    override val uiState: StateFlow<TasksState>
        get() = _uiState
    override val events: Flow<TasksSideEffect>
        get() = _events


    override fun getTask() {
        scope.launch {
            when (taskId) {
                null -> {
                    _uiState.update { TasksState.EmptyAddEditTask }
                }
                else -> addEditTask.getTaskToEdit(taskId).collectLatest { task ->
                    when (task) {
                        null -> _uiState.update { TasksState.EmptyAddEditTask }
                        else -> _uiState.update { TasksState.AddEditTask(task) }
                    }
                }
            }
        }
    }

    override fun saveTask(task: EditTask) {
        scope.launch {
            addEditTask.addTask(task)
            _events.update { TasksSideEffect.ShowSnackbar(Constants.TASK_SAVED) }
        }
    }

    override fun goBack() {
        _events.update { TasksSideEffect.Navigation.GoBack }
    }
}
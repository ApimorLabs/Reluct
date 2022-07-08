package work.racka.reluct.common.features.tasks.add_edit_task

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.AddEditTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

class AddEditTaskViewModel(
    private val modifyTaskUseCase: ModifyTaskUseCase,
    private val taskId: String?
) : CommonViewModel() {
    private val _uiState: MutableStateFlow<AddEditTasksState> =
        MutableStateFlow(AddEditTasksState.Loading)
    private val _events: Channel<TasksEvents> = Channel()

    val uiState: StateFlow<AddEditTasksState>
        get() = _uiState
    val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    init {
        getTask(taskId)
    }

    fun getTask(id: String?) {
        resetEvents()
        vmScope.launch {
            when (id) {
                null -> {
                    _uiState.update { AddEditTasksState.Data() }
                }
                else -> modifyTaskUseCase.getTaskToEdit(id).collectLatest { task ->
                    when (task) {
                        null -> _uiState.update { AddEditTasksState.Data() }
                        else -> _uiState.update { AddEditTasksState.Data(task) }
                    }
                }
            }
        }
    }

    fun saveTask(task: EditTask) {
        vmScope.launch {
            modifyTaskUseCase.saveTask(task)
            val result = _events.trySend(TasksEvents.ShowMessage(Constants.TASK_SAVED))
            result.onSuccess {
                println("AddEdit Task: $taskId")
                /**
                 * Go back after saving if you are just editing a Task and the [taskId]
                 * is not null else just show the TaskSaved State for adding more tasks
                 */
                if (taskId != null) {
                    _events.send(TasksEvents.Navigation.GoBack)
                } else {
                    _uiState.update { AddEditTasksState.TaskSaved }
                }
            }
        }
    }

    fun goBack() {
        _events.trySend(TasksEvents.Navigation.GoBack)
    }

    private fun resetEvents() {
        _events.trySend(TasksEvents.Nothing)
    }
}
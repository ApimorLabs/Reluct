package work.racka.reluct.common.features.tasks.task_details

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TaskDetailsState
import work.racka.reluct.common.model.states.tasks.TasksEvents

class TaskDetailsViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val taskId: String?,
) : CommonViewModel() {

    private val _uiState: MutableStateFlow<TaskDetailsState> =
        MutableStateFlow(TaskDetailsState.Loading)
    private val _events: Channel<TasksEvents> = Channel()

    val uiState: StateFlow<TaskDetailsState>
        get() = _uiState

    val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    init {
        getTask()
    }

    private fun getTask() {
        vmScope.launch {
            when (taskId) {
                null -> {
                    _uiState.update { TaskDetailsState.Data() }
                }
                else -> getTasksUseCase.getTask(taskId).collectLatest { task ->
                    when (task) {
                        null -> _uiState.update { TaskDetailsState.Data() }
                        else -> _uiState.update { TaskDetailsState.Data(task) }
                    }
                }
            }
        }
    }

    fun toggleDone(task: Task, isDone: Boolean) {
        modifyTasksUsesCase.toggleTaskDone(task, isDone)
        _events.trySend(TasksEvents.ShowMessageDone(isDone, task.title))
    }

    fun editTask(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToEdit(taskId))
    }

    fun deleteTask(taskId: String) {
        vmScope.launch {
            modifyTasksUsesCase.deleteTask(taskId)
            val result = _events.trySend(
                TasksEvents.ShowMessage(Constants.DELETED_SUCCESSFULLY)
            )
            result.onSuccess { _events.send(TasksEvents.Navigation.GoBack) }
        }
    }

    fun goBack() {
        _events.trySend(TasksEvents.Navigation.GoBack)
    }
}
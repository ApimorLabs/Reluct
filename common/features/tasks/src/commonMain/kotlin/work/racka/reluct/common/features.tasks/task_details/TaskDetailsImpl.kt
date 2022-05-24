package work.racka.reluct.common.features.tasks.task_details

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TaskDetailsState
import work.racka.reluct.common.model.states.tasks.TasksEvents

internal class TaskDetailsImpl(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val taskId: String?,
    private val scope: CoroutineScope,
) : TaskDetails {

    private val _uiState: MutableStateFlow<TaskDetailsState> =
        MutableStateFlow(TaskDetailsState.Loading)
    private val _events: Channel<TasksEvents> = Channel()

    override val uiState: StateFlow<TaskDetailsState>
        get() = _uiState

    override val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    init {
        getTask()
    }

    private fun getTask() {
        scope.launch {
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

    override fun toggleDone(task: Task, isDone: Boolean) {
        modifyTasksUsesCase.toggleTaskDone(task, isDone)
        _events.trySend(TasksEvents.ShowMessageDone(isDone))
    }

    override fun editTask(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToEdit(taskId))
    }

    override fun deleteTask(taskId: String) {
        scope.launch {
            modifyTasksUsesCase.deleteTask(taskId)
            val result = _events.trySend(
                TasksEvents.ShowMessage(Constants.DELETED_SUCCESSFULLY)
            )
            result.onSuccess { _events.send(TasksEvents.Navigation.GoBack) }
        }
    }

    override fun goBack() {
        _events.trySend(TasksEvents.Navigation.GoBack)
    }
}
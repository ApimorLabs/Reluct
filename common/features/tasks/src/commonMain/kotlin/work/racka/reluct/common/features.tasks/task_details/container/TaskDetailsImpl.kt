package work.racka.reluct.common.features.tasks.task_details.container

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.task_details.repository.TaskDetailsRepository
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

class TaskDetailsImpl(
    private val taskDetails: TaskDetailsRepository,
    private val taskId: Long?,
    private val scope: CoroutineScope,
) : TaskDetails {

    private val _uiState: MutableStateFlow<TasksState> =
        MutableStateFlow(TasksState.Loading)
    private val _events: Channel<TasksSideEffect> = Channel()

    override val uiState: StateFlow<TasksState>
        get() = _uiState

    override val events: Flow<TasksSideEffect>
        get() = _events.receiveAsFlow()

    init {
        getTask()
    }

    private fun getTask() {
        scope.launch {
            when (taskId) {
                null -> {
                    _uiState.update { TasksState.EmptyTaskDetails }
                    _events.send(
                        TasksSideEffect.DisplayErrorMsg(
                            Constants.TASK_NOT_FOUND
                        )
                    )
                }
                else -> taskDetails.getTask(taskId).collectLatest { task ->
                    when (task) {
                        null -> {
                            _uiState.update { TasksState.EmptyTaskDetails }
                            _events.send(
                                TasksSideEffect.DisplayErrorMsg(
                                    Constants.TASK_NOT_FOUND
                                )
                            )
                        }
                        else -> {
                            _uiState.update { TasksState.TaskDetails(task) }
                        }
                    }
                }
            }
        }
    }

    override fun toggleDone(taskId: Long, isDone: Boolean) {
        taskDetails.toggleTask(taskId, isDone)
        _events.trySend(TasksSideEffect.TaskDone(isDone))
    }

    override fun editTask(taskId: Long) {
        _events.trySend(TasksSideEffect.Navigation.NavigateToEdit(taskId))
    }

    override fun deleteTask(taskId: Long) {
        scope.launch {
            taskDetails.deleteTask(taskId)
            _events.send(
                TasksSideEffect.ShowSnackbar(Constants.DELETED_SUCCESSFULLY)
            )
        }
    }

    override fun goBack() {
        _events.trySend(TasksSideEffect.Navigation.GoBack)
    }
}
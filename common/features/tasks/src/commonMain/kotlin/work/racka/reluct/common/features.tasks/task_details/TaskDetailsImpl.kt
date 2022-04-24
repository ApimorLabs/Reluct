package work.racka.reluct.common.features.tasks.task_details

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

internal class TaskDetailsImpl(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTasksUseCase,
    private val taskId: String?,
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
                else -> getTasksUseCase.getTask(taskId).collectLatest { task ->
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

    override fun toggleDone(task: Task, isDone: Boolean) {
        modifyTasksUsesCase.toggleTaskDone(task, isDone)
        _events.trySend(TasksSideEffect.ShowMessageDone(isDone))
    }

    override fun editTask(taskId: String) {
        _events.trySend(TasksSideEffect.Navigation.NavigateToEdit(taskId))
    }

    override fun deleteTask(taskId: String) {
        scope.launch {
            modifyTasksUsesCase.deleteTask(taskId)
            _events.send(
                TasksSideEffect.ShowMessage(Constants.DELETED_SUCCESSFULLY)
            )
        }
    }

    override fun goBack() {
        _events.trySend(TasksSideEffect.Navigation.GoBack)
    }
}
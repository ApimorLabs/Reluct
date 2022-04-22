package work.racka.reluct.common.features.tasks.completed_tasks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

internal class CompletedTasksImpl(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTasksUseCase,
    private val scope: CoroutineScope,
) : CompletedTasks {

    private val _uiState: MutableStateFlow<TasksState> =
        MutableStateFlow(TasksState.Loading)
    private val _events: Channel<TasksSideEffect> = Channel()

    override val uiState: StateFlow<TasksState>
        get() = _uiState
    override val events: Flow<TasksSideEffect>
        get() = _events.receiveAsFlow()

    init {
        getCompletedTasks()
    }

    private fun getCompletedTasks() {
        scope.launch {
            getTasksUseCase.getCompletedTasks().collectLatest { taskList ->
                val grouped = taskList.groupBy { it.dueDate }
                _uiState.update {
                    TasksState.CompletedTasks(
                        tasks = grouped
                    )
                }
            }
        }
    }

    override fun toggleDone(task: Task, isDone: Boolean) {
        modifyTasksUsesCase.toggleTaskDone(task, isDone)
        _events.trySend(TasksSideEffect.ShowMessageDone(isDone))
    }

    override fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksSideEffect.Navigation.NavigateToTaskDetails(taskId))
    }
}
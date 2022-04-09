package work.racka.reluct.common.features.tasks.completed_tasks.container

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.completed_tasks.repository.CompletedTasksRepository
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

class CompletedTasksImpl(
    private val completedTasks: CompletedTasksRepository,
    private val scope: CoroutineScope
) : CompletedTasks {

    private val _uiState: MutableStateFlow<TasksState> =
        MutableStateFlow(TasksState.Loading)
    private val _events: MutableStateFlow<TasksSideEffect> =
        MutableStateFlow(TasksSideEffect.Nothing)

    override val uiState: StateFlow<TasksState>
        get() = _uiState
    override val events: Flow<TasksSideEffect>
        get() = _events

    init {
        getCompletedTasks()
    }

    private fun getCompletedTasks() {
        scope.launch {
            completedTasks.getTasks().collectLatest { taskList ->
                val grouped = taskList.groupBy { it.dueDate }
                _uiState.update {
                    TasksState.CompletedTasks(
                        tasks = grouped
                    )
                }
            }
        }
    }

    override fun toggleDone(taskId: Long, isDone: Boolean) {
        scope.launch {
            completedTasks.toggleTaskDone(taskId, isDone)
            _events.update {
                TasksSideEffect.TaskDone(isDone)
            }
        }
    }

    override fun navigateToTaskDetails(taskId: Long) {
        _events.update { TasksSideEffect.Navigation.NavigateToTaskDetails(taskId) }
    }
}
package work.racka.reluct.common.features.tasks.completed_tasks.container

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.completed_tasks.repository.CompletedTasksRepository
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

internal class CompletedTasksImpl(
    private val completedTasks: CompletedTasksRepository,
    private val scope: CoroutineScope
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

    override fun toggleDone(taskId: String, isDone: Boolean) {
        completedTasks.toggleTaskDone(taskId, isDone)
        _events.trySend(TasksSideEffect.ShowMessageDone(isDone))
    }

    override fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksSideEffect.Navigation.NavigateToTaskDetails(taskId))
    }
}
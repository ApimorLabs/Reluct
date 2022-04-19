package work.racka.reluct.common.features.tasks.pending_tasks.container

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.pending_tasks.repository.PendingTasksRepository
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

internal class PendingTasksImpl(
    private val pendingTasks: PendingTasksRepository,
    private val scope: CoroutineScope
) : PendingTasks {

    private val _uiState: MutableStateFlow<TasksState> =
        MutableStateFlow(TasksState.Loading)
    private val _events: Channel<TasksSideEffect> = Channel()

    override val uiState: StateFlow<TasksState>
        get() = _uiState

    override val events: Flow<TasksSideEffect>
        get() = _events.receiveAsFlow()

    init {
        getPendingTasks()
    }

    private fun getPendingTasks() {
        scope.launch {
            pendingTasks.getTasks().collectLatest { taskList ->
                val overdueList = taskList.filter { it.overdue }
                val grouped = taskList
                    .filterNot { it.overdue }
                    .groupBy { it.dueDate }
                _uiState.update {
                    TasksState.PendingTasks(
                        tasks = grouped,
                        overdueTasks = overdueList
                    )
                }
            }
        }
    }

    override fun toggleDone(taskId: String, isDone: Boolean) {
        pendingTasks.toggleTaskDone(taskId, isDone)
        _events.trySend(TasksSideEffect.ShowMessageTaskDone(isDone))
    }

    override fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksSideEffect.Navigation.NavigateToTaskDetails(taskId))
    }
}
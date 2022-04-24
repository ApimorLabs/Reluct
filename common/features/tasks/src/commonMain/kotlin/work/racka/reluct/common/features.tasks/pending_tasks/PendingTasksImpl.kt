package work.racka.reluct.common.features.tasks.pending_tasks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

internal class PendingTasksImpl(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTasksUseCase,
    private val scope: CoroutineScope,
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
            getTasksUseCase.getPendingTasks().collectLatest { taskList ->
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

    override fun toggleDone(task: Task, isDone: Boolean) {
        modifyTasksUsesCase.toggleTaskDone(task, isDone)
        _events.trySend(TasksSideEffect.ShowMessageDone(isDone))
    }

    override fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksSideEffect.Navigation.NavigateToTaskDetails(taskId))
    }
}
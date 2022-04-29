package work.racka.reluct.common.features.tasks.pending_tasks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.PendingTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

internal class PendingTasksImpl(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTasksUseCase,
    private val scope: CoroutineScope,
) : PendingTasks {

    private val _uiState: MutableStateFlow<PendingTasksState> =
        MutableStateFlow(PendingTasksState.Loading)
    private val _events: Channel<TasksEvents> = Channel()

    override val uiState: StateFlow<PendingTasksState>
        get() = _uiState

    override val events: Flow<TasksEvents>
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
                    PendingTasksState.Data(
                        tasks = grouped,
                        overdueTasks = overdueList
                    )
                }
            }
        }
    }

    override fun toggleDone(task: Task, isDone: Boolean) {
        modifyTasksUsesCase.toggleTaskDone(task, isDone)
        _events.trySend(TasksEvents.ShowMessageDone(isDone))
    }

    override fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToTaskDetails(taskId))
    }
}
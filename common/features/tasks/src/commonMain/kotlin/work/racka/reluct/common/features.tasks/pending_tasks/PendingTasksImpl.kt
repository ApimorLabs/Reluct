package work.racka.reluct.common.features.tasks.pending_tasks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.PendingTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

internal class PendingTasksImpl(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val scope: CoroutineScope,
) : PendingTasks {

    private val _uiState: MutableStateFlow<PendingTasksState> =
        MutableStateFlow(PendingTasksState.Loading())
    private val _events: Channel<TasksEvents> = Channel()

    override val uiState: StateFlow<PendingTasksState>
        get() = _uiState

    override val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    private var limitFactor = 1L
    private var newDataPresent = true

    private lateinit var pendingTasksJob: Job

    init {
        getPendingTasks(limitFactor)
    }

    private fun getPendingTasks(limitFactor: Long) {
        pendingTasksJob = scope.launch {
            getTasksUseCase.getPendingTasks(factor = limitFactor).collectLatest { taskList ->
                val overdueList = taskList.filter { it.overdue }
                val grouped = taskList
                    .filterNot { it.overdue }
                    .groupBy { it.dueDate }
                _uiState.update {
                    newDataPresent = it.tasksData != grouped
                    PendingTasksState.Data(
                        tasks = grouped,
                        overdueTasks = overdueList,
                        newDataPresent = newDataPresent
                    )
                }
            }
        }
    }

    override fun fetchMoreData() {
        if (newDataPresent) {
            limitFactor++
            pendingTasksJob.cancel()
            _uiState.update {
                PendingTasksState.Loading(
                    tasks = it.tasksData,
                    overdueTasks = it.overdueTasksData,
                    newDataPresent = newDataPresent
                )
            }
            getPendingTasks(limitFactor)
        }
    }

    override fun toggleDone(task: Task, isDone: Boolean) {
        modifyTasksUsesCase.toggleTaskDone(task, isDone)
        _events.trySend(TasksEvents.ShowMessageDone(isDone, task.title))
    }

    override fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToTaskDetails(taskId))
    }
}
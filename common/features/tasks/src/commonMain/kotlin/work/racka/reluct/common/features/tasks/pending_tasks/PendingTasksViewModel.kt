package work.racka.reluct.common.features.tasks.pending_tasks

import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.domain.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.domain.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.PendingTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents
import work.racka.reluct.common.model.util.list.filterPersistent

class PendingTasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase
) : CommonViewModel() {

    private val _uiState: MutableStateFlow<PendingTasksState> =
        MutableStateFlow(PendingTasksState.Loading())
    private val _events: Channel<TasksEvents> = Channel()

    val uiState: StateFlow<PendingTasksState>
        get() = _uiState

    val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    private var limitFactor = 1L
    private var newDataPresent = true

    private lateinit var pendingTasksJob: Job

    init {
        getPendingTasks(limitFactor)
    }

    private fun getPendingTasks(limitFactor: Long) {
        pendingTasksJob = vmScope.launch {
            getTasksUseCase.getPendingTasks(factor = limitFactor).collectLatest { taskList ->
                val overdueList = taskList.filterPersistent { it.overdue }
                val grouped = taskList
                    .filterNot { it.overdue }
                    .groupBy { it.dueDate }
                    .mapValues { it.value.toImmutableList() }
                    .toImmutableMap()
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

    fun fetchMoreData() {
        if (newDataPresent) {
            limitFactor++
            pendingTasksJob.cancel()
            _uiState.update {
                PendingTasksState.Loading(
                    tasks = it.tasksData,
                    overdueTasks = it.overdueTasksData,
                    newDataPresent = it.shouldUpdateData
                )
            }
            getPendingTasks(limitFactor)
        }
    }

    fun toggleDone(task: Task, isDone: Boolean) {
        vmScope.launch {
            modifyTasksUsesCase.toggleTaskDone(task, isDone)
            _events.send(TasksEvents.ShowMessageDone(isDone, task.title))
        }
    }

    fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToTaskDetails(taskId))
    }
}
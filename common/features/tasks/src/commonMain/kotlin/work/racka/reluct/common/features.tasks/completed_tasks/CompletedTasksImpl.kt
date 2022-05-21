package work.racka.reluct.common.features.tasks.completed_tasks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.CompletedTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

internal class CompletedTasksImpl(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val scope: CoroutineScope,
) : CompletedTasks {

    private val _uiState: MutableStateFlow<CompletedTasksState> =
        MutableStateFlow(CompletedTasksState.Loading())
    private val _events: Channel<TasksEvents> = Channel()

    override val uiState: StateFlow<CompletedTasksState>
        get() = _uiState
    override val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    private var limitFactor = 1L
    private var newDataPresent = true

    private lateinit var completedTasksJob: Job

    init {
        getCompletedTasks(factor = limitFactor)
    }

    private fun getCompletedTasks(factor: Long = 1) {
        completedTasksJob = scope.launch {
            getTasksUseCase.getCompletedTasks(factor = factor).collectLatest { taskList ->
                val grouped = taskList.groupBy { it.dueDate }
                _uiState.update {
                    newDataPresent = it.tasksData != grouped
                    CompletedTasksState.Data(
                        tasks = grouped,
                        newDataPresent = newDataPresent
                    )
                }
            }
        }
    }

    override fun fetchMoreData() {
        if (newDataPresent) {
            limitFactor++
            completedTasksJob.cancel()
            _uiState.update {
                CompletedTasksState.Loading(
                    tasks = it.tasksData,
                    newDataPresent = newDataPresent
                )
            }
            getCompletedTasks(limitFactor)
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
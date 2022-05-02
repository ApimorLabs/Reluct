package work.racka.reluct.common.features.tasks.completed_tasks

import kotlinx.coroutines.CoroutineScope
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
        MutableStateFlow(CompletedTasksState.Loading)
    private val _events: Channel<TasksEvents> = Channel()

    override val uiState: StateFlow<CompletedTasksState>
        get() = _uiState
    override val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    init {
        getCompletedTasks()
    }

    private fun getCompletedTasks() {
        scope.launch {
            getTasksUseCase.getCompletedTasks().collectLatest { taskList ->
                val grouped = taskList.groupBy { it.dueDate }
                _uiState.update {
                    CompletedTasksState.Data(
                        tasks = grouped
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
package work.racka.reluct.common.features.tasks.task_details

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.domain.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.domain.usecases.tasks.ManageTaskLabels
import work.racka.reluct.common.domain.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.states.TaskDetailsState
import work.racka.reluct.common.features.tasks.states.TaskState
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.domain.tasks.TaskLabel
import work.racka.reluct.common.model.states.tasks.TasksEvents

class TaskDetailsViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val manageTaskLabels: ManageTaskLabels,
    private val taskId: String?,
) : CommonViewModel() {

    private val taskState: MutableStateFlow<TaskState> = MutableStateFlow(TaskState.Loading)
    private val availableTaskLabels: MutableStateFlow<ImmutableList<TaskLabel>> =
        MutableStateFlow(persistentListOf())

    private val eventsChannel: Channel<TasksEvents> = Channel()

    val uiState: StateFlow<TaskDetailsState> = combine(
        taskState, availableTaskLabels
    ) { taskState, availableTaskLabels ->
        TaskDetailsState(
            taskState = taskState,
            availableTaskLabels = availableTaskLabels
        )
    }.stateIn(
        scope = vmScope,
        initialValue = TaskDetailsState(),
        started = SharingStarted.WhileSubscribed(5_000)
    )

    val events: Flow<TasksEvents> = eventsChannel.receiveAsFlow()

    private var getTaskJob: Job? = null

    init {
        getTask()
        getTaskLabels()
    }

    private fun getTaskLabels() {
        vmScope.launch {
            manageTaskLabels.allLabels().collectLatest { labels ->
                availableTaskLabels.update { labels }
            }
        }
    }

    private fun getTask() {
        getTaskJob?.cancel()
        getTaskJob = vmScope.launch {
            when (taskId) {
                null -> {
                    taskState.update { TaskState.NotFound }
                }
                else -> getTasksUseCase.getTask(taskId).collectLatest { task ->
                    when (task) {
                        null -> taskState.update { TaskState.NotFound }
                        else -> taskState.update { TaskState.Data(task) }
                    }
                }
            }
        }
    }

    fun toggleDone(task: Task, isDone: Boolean) {
        vmScope.launch {
            modifyTasksUsesCase.toggleTaskDone(task, isDone)
            eventsChannel.send(TasksEvents.ShowMessageDone(isDone, task.title))
        }
    }

    fun editTask(taskId: String) {
        eventsChannel.trySend(TasksEvents.Navigation.NavigateToEdit(taskId))
    }

    fun deleteTask(taskId: String) {
        vmScope.launch {
            modifyTasksUsesCase.deleteTask(taskId)
            getTaskJob?.cancel()
            taskState.update { TaskState.Deleted }
            val result = eventsChannel.trySend(
                TasksEvents.ShowMessage(Constants.DELETED_SUCCESSFULLY)
            )
            result.onSuccess { eventsChannel.send(TasksEvents.Navigation.GoBack) }
        }
    }

    fun saveLabel(label: TaskLabel) {
        vmScope.launch {
            manageTaskLabels.addLabel(label)
        }
    }

    fun deleteLabel(label: TaskLabel) {
        vmScope.launch {
            manageTaskLabels.deleteLabel(label.id)
        }
    }

    fun goBack() {
        eventsChannel.trySend(TasksEvents.Navigation.GoBack)
    }
}
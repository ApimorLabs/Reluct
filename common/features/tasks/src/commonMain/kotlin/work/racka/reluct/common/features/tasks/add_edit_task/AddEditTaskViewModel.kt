package work.racka.reluct.common.features.tasks.add_edit_task

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.domain.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.AddEditTasksState
import work.racka.reluct.common.model.states.tasks.ModifyTaskState
import work.racka.reluct.common.model.states.tasks.TasksEvents

class AddEditTaskViewModel(
    private val modifyTaskUseCase: ModifyTaskUseCase,
    private val taskId: String?
) : CommonViewModel() {
    private val _uiState: MutableStateFlow<AddEditTasksState> =
        MutableStateFlow(AddEditTasksState.Loading)

    private val modifyTaskState: MutableStateFlow<ModifyTaskState> =
        MutableStateFlow(ModifyTaskState.Loading)
    val uiState: StateFlow<ModifyTaskState> = modifyTaskState.asStateFlow()

    private val eventsChannel: Channel<TasksEvents> = Channel()
    val events: Flow<TasksEvents>
        get() = eventsChannel.receiveAsFlow()

    init {
        getTask1(taskId)
    }

    fun saveCurrentTask() {
        vmScope.launch {
            val taskState = modifyTaskState.value
            if (taskState is ModifyTaskState.Data) {
                modifyTaskUseCase.saveTask(taskState.task)
                val result = eventsChannel.trySend(TasksEvents.ShowMessage(Constants.TASK_SAVED))
                result.onSuccess {
                    /**
                     * Go back after saving if you are just editing a Task and the [taskId]
                     * is not null else just show the TaskSaved State for adding more tasks
                     */
                    if (taskId != null) {
                        eventsChannel.send(TasksEvents.Navigation.GoBack)
                    } else {
                        modifyTaskState.update { ModifyTaskState.Saved }
                    }
                }
            }
        }
    }

    fun newTask() {
        modifyTaskState.update {
            ModifyTaskState.Data(
                isEdit = false,
                task = DefaultTasks.emptyEditTask()
            )
        }
    }

    fun updateCurrentTask(task: EditTask) {
        modifyTaskState.update { ModifyTaskState.Data(isEdit = taskId != null, task = task) }
    }

    fun getTask(id: String?) {
        resetEvents()
        vmScope.launch {
            when (id) {
                null -> {
                    _uiState.update { AddEditTasksState.Data() }
                }
                else -> modifyTaskUseCase.getTaskToEdit(id).collectLatest { task ->
                    when (task) {
                        null -> _uiState.update { AddEditTasksState.Data() }
                        else -> _uiState.update { AddEditTasksState.Data(task) }
                    }
                }
            }
        }
    }

    fun saveTask(task: EditTask) {
        vmScope.launch {
            modifyTaskUseCase.saveTask(task)
            val result = eventsChannel.trySend(TasksEvents.ShowMessage(Constants.TASK_SAVED))
            result.onSuccess {
                println("AddEdit Task: $taskId")
                /**
                 * Go back after saving if you are just editing a Task and the [taskId]
                 * is not null else just show the TaskSaved State for adding more tasks
                 */
                if (taskId != null) {
                    eventsChannel.send(TasksEvents.Navigation.GoBack)
                } else {
                    _uiState.update { AddEditTasksState.TaskSaved }
                }
            }
        }
    }

    private fun getTask1(taskId: String?) {
        vmScope.launch {
            when (taskId) {
                null -> modifyTaskState.update {
                    ModifyTaskState.Data(isEdit = false, task = DefaultTasks.emptyEditTask())
                }
                else -> {
                    when (val task = modifyTaskUseCase.getTaskToEdit(taskId).firstOrNull()) {
                        null -> modifyTaskState.update { ModifyTaskState.NotFound }
                        else -> modifyTaskState.update {
                            ModifyTaskState.Data(
                                isEdit = true,
                                task = task
                            )
                        }
                    }
                }
            }
        }
    }

    fun goBack() {
        eventsChannel.trySend(TasksEvents.Navigation.GoBack)
    }

    private fun resetEvents() {
        eventsChannel.trySend(TasksEvents.Nothing)
    }
}
package work.racka.reluct.common.features.tasks.add_edit_task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.AddEditTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

internal class AddEditTaskImpl(
    private val modifyTasksUseCase: ModifyTasksUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val taskId: String?,
    private val scope: CoroutineScope,
) : AddEditTask {
    private val _uiState: MutableStateFlow<AddEditTasksState> =
        MutableStateFlow(AddEditTasksState.Loading)
    private val _events: Channel<TasksEvents> = Channel()

    override val uiState: StateFlow<AddEditTasksState>
        get() = _uiState
    override val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    init {
        getTask(taskId)
    }

    override fun getTask(id: String?) {
        resetEvents()
        scope.launch {
            when (id) {
                null -> {
                    _uiState.update { AddEditTasksState.Data() }
                }
                else -> getTasksUseCase.getTaskToEdit(id).take(1)
                    .collectLatest { task ->
                        when (task) {
                            null -> _uiState.update { AddEditTasksState.Data() }
                            else -> _uiState.update { AddEditTasksState.Data(task) }
                        }
                    }
            }
        }
    }

    override fun saveTask(task: EditTask) {
        scope.launch {
            modifyTasksUseCase.addTask(task)
            val result = _events.trySend(TasksEvents.ShowMessage(Constants.TASK_SAVED))
            result.onSuccess {
                println("AddEdit Task: $taskId")
                /**
                 * Go back after saving if you are just editing a Task and the [taskId]
                 * is not null else just show the TaskSaved State for adding more tasks
                 */
                if (taskId != null) {
                    _events.send(TasksEvents.Navigation.GoBack)
                } else {
                    _uiState.update { AddEditTasksState.TaskSaved }
                }
            }
        }
    }

    override fun goBack() {
        _events.trySend(TasksEvents.Navigation.GoBack)
    }

    private fun resetEvents() {
        _events.trySend(TasksEvents.Nothing)
    }
}
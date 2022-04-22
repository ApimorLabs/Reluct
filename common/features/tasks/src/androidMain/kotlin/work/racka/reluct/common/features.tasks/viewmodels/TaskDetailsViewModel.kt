package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.tasks.task_details.TaskDetails
import work.racka.reluct.common.features.tasks.task_details.TaskDetailsImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

actual class TaskDetailsViewModel internal constructor(
    getTasksUseCase: GetTasksUseCase,
    modifyTasksUsesCase: ModifyTasksUseCase,
    taskId: String?,
) : ViewModel() {
    private val host: TaskDetails by lazy {
        TaskDetailsImpl(
            getTasksUseCase = getTasksUseCase,
            modifyTasksUsesCase = modifyTasksUsesCase,
            taskId = taskId,
            scope = viewModelScope
        )
    }

    actual val uiState: StateFlow<TasksState> = host.uiState
    actual val events: Flow<TasksSideEffect> = host.events
    actual fun deleteTask(taskId: String) = host.deleteTask(taskId)
    actual fun toggleDone(task: Task, isDone: Boolean) = host.toggleDone(task, isDone)
    actual fun editTask(taskId: String) = host.editTask(taskId)
    actual fun goBack() = host.goBack()
}
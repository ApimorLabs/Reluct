package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetails
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsImpl
import work.racka.reluct.common.features.tasks.task_details.repository.TaskDetailsRepository
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

actual class TaskDetailsViewModel(
    taskDetails: TaskDetailsRepository,
    taskId: String?,
    scope: CoroutineScope,
) {
    private val host: TaskDetails by lazy {
        TaskDetailsImpl(
            taskDetails = taskDetails,
            taskId = taskId,
            scope = scope
        )
    }

    actual val uiState: StateFlow<TasksState> = host.uiState
    actual val events: Flow<TasksSideEffect> = host.events
    actual fun deleteTask(taskId: String) = host.deleteTask(taskId)
    actual fun toggleDone(task: Task, isDone: Boolean) = host.toggleDone(task, isDone)
    actual fun editTask(taskId: String) = host.editTask(taskId)
    actual fun goBack() = host.goBack()
}
package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasksImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.PendingTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

actual class PendingTasksViewModel internal constructor(
    getTasksUseCase: GetTasksUseCase,
    modifyTasksUsesCase: ModifyTaskUseCase,
    scope: CoroutineScope,
) {
    private val host: PendingTasks by lazy {
        PendingTasksImpl(
            getTasksUseCase = getTasksUseCase,
            modifyTasksUsesCase = modifyTasksUsesCase,
            scope = scope
        )
    }

    actual val uiState: StateFlow<PendingTasksState> = host.uiState

    actual val events: Flow<TasksEvents> = host.events

    actual fun toggleDone(task: Task, isDone: Boolean) = host.toggleDone(task, isDone)

    actual fun navigateToTaskDetails(taskId: String) = host.navigateToTaskDetails(taskId)
}
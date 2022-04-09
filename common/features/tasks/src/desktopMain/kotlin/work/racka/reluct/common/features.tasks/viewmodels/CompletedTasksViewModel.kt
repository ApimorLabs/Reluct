package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.tasks.completed_tasks.container.CompletedTasks
import work.racka.reluct.common.features.tasks.completed_tasks.container.CompletedTasksImpl
import work.racka.reluct.common.features.tasks.completed_tasks.repository.CompletedTasksRepository
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

actual class CompletedTasksViewModel(
    completedTasks: CompletedTasksRepository,
    scope: CoroutineScope
) {
    private val host: CompletedTasks by lazy {
        CompletedTasksImpl(
            completedTasks = completedTasks,
            scope = scope
        )
    }

    actual val uiState: StateFlow<TasksState> = host.uiState

    actual val events: Flow<TasksSideEffect> = host.events

    actual fun toggleDone(taskId: Long, isDone: Boolean) = host.toggleDone(taskId, isDone)

    actual fun navigateToTaskDetails(taskId: Long) = host.navigateToTaskDetails(taskId)
}
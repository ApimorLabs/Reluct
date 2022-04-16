package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasks
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasksImpl
import work.racka.reluct.common.features.tasks.pending_tasks.repository.PendingTasksRepository
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

actual class PendingTasksViewModel(
    pendingTasks: PendingTasksRepository
) : ViewModel() {
    private val host: PendingTasks by lazy {
        PendingTasksImpl(
            pendingTasks = pendingTasks,
            scope = viewModelScope
        )
    }

    actual val uiState: StateFlow<TasksState> = host.uiState

    actual val events: Flow<TasksSideEffect> = host.events

    actual fun toggleDone(taskId: String, isDone: Boolean) = host.toggleDone(taskId, isDone)

    actual fun navigateToTaskDetails(taskId: String) = host.navigateToTaskDetails(taskId)
}
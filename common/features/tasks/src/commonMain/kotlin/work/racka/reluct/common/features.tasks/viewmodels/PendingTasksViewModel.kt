package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

expect class PendingTasksViewModel {
    val uiState: StateFlow<TasksState>
    val events: Flow<TasksSideEffect>
    fun toggleDone(taskId: String, isDone: Boolean)
    fun navigateToTaskDetails(taskId: String)
}
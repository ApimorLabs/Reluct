package work.racka.reluct.common.features.tasks.pending_tasks.container

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

interface PendingTasksContainerHost {
    val uiState: StateFlow<TasksState>
    val sideEffect: Flow<TasksSideEffect>
    fun toggleDone(taskId: Long, isDone: Boolean)
    fun navigateToTaskDetails(taskId: Long)
}
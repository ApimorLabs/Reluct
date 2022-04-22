package work.racka.reluct.common.features.tasks.completed_tasks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

internal interface CompletedTasks {
    val uiState: StateFlow<TasksState>
    val events: Flow<TasksSideEffect>
    fun toggleDone(task: Task, isDone: Boolean)
    fun navigateToTaskDetails(taskId: String)
}
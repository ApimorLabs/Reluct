package work.racka.reluct.common.features.tasks.pending_tasks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.PendingTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

internal interface PendingTasks {
    val uiState: StateFlow<PendingTasksState>
    val events: Flow<TasksEvents>
    fun toggleDone(task: Task, isDone: Boolean)
    fun navigateToTaskDetails(taskId: String)
}
package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

expect class SearchTasksViewModel {
    val uiState: StateFlow<TasksState>
    val events: Flow<TasksSideEffect>
    fun searchTasks(query: String)
    fun toggleDone(taskId: Long, isDone: Boolean)
    fun navigateToTaskDetails(taskId: Long)
    fun goBack()
}
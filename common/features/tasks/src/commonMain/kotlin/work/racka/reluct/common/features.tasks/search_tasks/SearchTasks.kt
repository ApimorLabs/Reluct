package work.racka.reluct.common.features.tasks.search_tasks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.SearchTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

interface SearchTasks {
    val uiState: StateFlow<SearchTasksState>
    val events: Flow<TasksEvents>
    fun search(query: String)
    fun fetchMoreData()
    fun toggleDone(task: Task, isDone: Boolean)
    fun navigateToTaskDetails(taskId: String)
    fun goBack()
}
package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.tasks.search.container.SearchTasks
import work.racka.reluct.common.features.tasks.search.container.SearchTasksImpl
import work.racka.reluct.common.features.tasks.search.repository.SearchTasksRepository
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

actual class SearchTasksViewModel(
    searchTasks: SearchTasksRepository
) : ViewModel() {

    private val host: SearchTasks by lazy {
        SearchTasksImpl(
            searchTasks = searchTasks,
            scope = viewModelScope
        )
    }

    actual val uiState: StateFlow<TasksState> = host.uiState

    actual val events: Flow<TasksSideEffect> = host.events

    actual fun searchTasks(query: String) = host.searchTasks(query)

    actual fun toggleDone(taskId: String, isDone: Boolean) = host.toggleDone(taskId, isDone)

    actual fun navigateToTaskDetails(taskId: String) = host.navigateToTaskDetails(taskId)

    actual fun goBack() = host.goBack()
}
package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import work.racka.reluct.common.features.tasks.search.SearchTasks
import work.racka.reluct.common.features.tasks.search.container.SearchTasksContainerHost
import work.racka.reluct.common.features.tasks.search.container.SearchTasksContainerHostImpl

actual class SearchTasksViewModel(
    searchTasks: SearchTasks,
    backgroundDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val host: SearchTasksContainerHost by lazy {
        SearchTasksContainerHostImpl(
            searchTasks = searchTasks,
            backgroundDispatcher = backgroundDispatcher,
            scope = viewModelScope
        )
    }
}
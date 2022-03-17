package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import work.racka.reluct.common.features.tasks.search.container.SearchTasksContainerHost
import work.racka.reluct.common.features.tasks.search.container.SearchTasksContainerHostImpl
import work.racka.reluct.common.features.tasks.search.repository.SearchTasksRepository

actual class SearchTasksViewModel(
    searchTasks: SearchTasksRepository
) : ViewModel() {

    val host: SearchTasksContainerHost by lazy {
        SearchTasksContainerHostImpl(
            searchTasks = searchTasks,
            scope = viewModelScope
        )
    }
}
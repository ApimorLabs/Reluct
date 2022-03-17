package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.search.container.SearchTasksContainerHost
import work.racka.reluct.common.features.tasks.search.container.SearchTasksContainerHostImpl
import work.racka.reluct.common.features.tasks.search.repository.SearchTasksRepository

actual class SearchTasksViewModel(
    searchTasks: SearchTasksRepository,
    scope: CoroutineScope
) {
    val host: SearchTasksContainerHost by lazy {
        SearchTasksContainerHostImpl(
            searchTasks = searchTasks,
            scope = scope
        )
    }
}
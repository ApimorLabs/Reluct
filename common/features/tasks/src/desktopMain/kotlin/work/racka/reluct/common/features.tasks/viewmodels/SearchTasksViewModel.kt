package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.search.SearchTasks
import work.racka.reluct.common.features.tasks.search.container.SearchTasksContainerHost
import work.racka.reluct.common.features.tasks.search.container.SearchTasksContainerHostImpl

actual class SearchTasksViewModel(
    searchTasks: SearchTasks,
    backgroundDispatcher: CoroutineDispatcher,
    scope: CoroutineScope
) {
    val host: SearchTasksContainerHost by lazy {
        SearchTasksContainerHostImpl(
            searchTasks = searchTasks,
            backgroundDispatcher = backgroundDispatcher,
            scope = scope
        )
    }
}
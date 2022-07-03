package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.tasks.search_tasks.SearchTasks

actual class SearchTasksViewModel(scope: CoroutineScope) {
    actual val host: SearchTasks by inject(SearchTasks::class.java) {
        parametersOf(scope)
    }
}
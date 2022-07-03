package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.tasks.search_tasks.SearchTasks

actual class SearchTasksViewModel : ViewModel() {
    actual val host: SearchTasks by inject(SearchTasks::class.java) {
        parametersOf(viewModelScope)
    }
}
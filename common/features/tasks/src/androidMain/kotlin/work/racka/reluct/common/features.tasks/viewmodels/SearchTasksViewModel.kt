package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.search_tasks.SearchTasks
import work.racka.reluct.common.features.tasks.search_tasks.SearchTasksImpl

actual class SearchTasksViewModel internal constructor(
    getTasksUseCase: GetTasksUseCase,
    modifyTasksUsesCase: ModifyTaskUseCase,
) : ViewModel() {
    actual val host: SearchTasks by lazy {
        SearchTasksImpl(
            getTasksUseCase = getTasksUseCase,
            modifyTasksUsesCase = modifyTasksUsesCase,
            scope = viewModelScope
        )
    }
}
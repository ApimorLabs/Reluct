package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.search_tasks.SearchTasks
import work.racka.reluct.common.features.tasks.search_tasks.SearchTasksImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase

actual class SearchTasksViewModel internal constructor(
    getTasksUseCase: GetTasksUseCase,
    modifyTasksUsesCase: ModifyTaskUseCase,
    scope: CoroutineScope,
) {
    actual val host: SearchTasks by lazy {
        SearchTasksImpl(
            getTasksUseCase = getTasksUseCase,
            modifyTasksUsesCase = modifyTasksUsesCase,
            scope = scope
        )
    }
}
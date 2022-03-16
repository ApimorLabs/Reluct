package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasks
import work.racka.reluct.common.features.tasks.completed_tasks.container.CompletedTasksContainerHost
import work.racka.reluct.common.features.tasks.completed_tasks.container.CompletedTasksContainerHostImpl

actual class CompletedTasksViewModel(
    completedTasks: CompletedTasks,
    backgroundDispatcher: CoroutineDispatcher,
    scope: CoroutineScope
) {
    val host: CompletedTasksContainerHost by lazy {
        CompletedTasksContainerHostImpl(
            completedTasks = completedTasks,
            backgroundDispatcher = backgroundDispatcher,
            scope = scope
        )
    }
}
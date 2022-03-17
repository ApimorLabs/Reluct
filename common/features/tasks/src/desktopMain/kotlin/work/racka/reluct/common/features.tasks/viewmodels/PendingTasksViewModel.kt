package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasksContainerHost
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasksContainerHostImpl
import work.racka.reluct.common.features.tasks.pending_tasks.repository.PendingTasksRepository

actual class PendingTasksViewModel(
    pendingTasks: PendingTasksRepository,
    backgroundDispatcher: CoroutineDispatcher,
    scope: CoroutineScope
) {
    val host: PendingTasksContainerHost by lazy {
        PendingTasksContainerHostImpl(
            pendingTasks = pendingTasks,
            backgroundDispatcher = backgroundDispatcher,
            scope = scope
        )
    }
}
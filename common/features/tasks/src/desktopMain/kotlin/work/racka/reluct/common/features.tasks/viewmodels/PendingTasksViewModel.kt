package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasksContainerHost
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasksContainerHostImpl

actual class PendingTasksViewModel(
    pendingTasks: PendingTasks,
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
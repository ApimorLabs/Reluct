package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasksContainerHost
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasksContainerHostImpl
import work.racka.reluct.common.features.tasks.pending_tasks.repository.PendingTasksRepository

actual class PendingTasksViewModel(
    pendingTasks: PendingTasksRepository,
    scope: CoroutineScope
) {
    val host: PendingTasksContainerHost by lazy {
        PendingTasksContainerHostImpl(
            pendingTasks = pendingTasks,
            scope = scope
        )
    }
}
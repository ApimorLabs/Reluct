package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasksContainerHost
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasksContainerHostImpl
import work.racka.reluct.common.features.tasks.pending_tasks.repository.PendingTasksRepository

actual class PendingTasksViewModel(
    pendingTasks: PendingTasksRepository
) : ViewModel() {
    val host: PendingTasksContainerHost by lazy {
        PendingTasksContainerHostImpl(
            pendingTasks = pendingTasks,
            scope = viewModelScope
        )
    }
}
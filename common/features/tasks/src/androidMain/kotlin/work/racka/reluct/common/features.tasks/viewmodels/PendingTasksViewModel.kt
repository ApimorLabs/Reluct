package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasksContainerHost
import work.racka.reluct.common.features.tasks.pending_tasks.container.PendingTasksContainerHostImpl

actual class PendingTasksViewModel(
    pendingTasks: PendingTasks,
    backgroundDispatcher: CoroutineDispatcher
) : ViewModel() {
    val host: PendingTasksContainerHost by lazy {
        PendingTasksContainerHostImpl(
            pendingTasks = pendingTasks,
            backgroundDispatcher = backgroundDispatcher,
            scope = viewModelScope
        )
    }
}
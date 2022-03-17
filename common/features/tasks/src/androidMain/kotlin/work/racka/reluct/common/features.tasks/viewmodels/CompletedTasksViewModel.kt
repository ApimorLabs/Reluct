package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import work.racka.reluct.common.features.tasks.completed_tasks.container.CompletedTasksContainerHost
import work.racka.reluct.common.features.tasks.completed_tasks.container.CompletedTasksContainerHostImpl
import work.racka.reluct.common.features.tasks.completed_tasks.repository.CompletedTasksRepository

actual class CompletedTasksViewModel(
    completedTasks: CompletedTasksRepository,
    backgroundDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val host: CompletedTasksContainerHost by lazy {
        CompletedTasksContainerHostImpl(
            completedTasks = completedTasks,
            backgroundDispatcher = backgroundDispatcher,
            scope = viewModelScope
        )
    }
}
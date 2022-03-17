package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHost
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHostImpl
import work.racka.reluct.common.features.tasks.task_details.repository.TaskDetailsRepository

actual class TaskDetailsViewModel(
    taskDetails: TaskDetailsRepository,
    backgroundDispatcher: CoroutineDispatcher
) : ViewModel() {
    val host: TaskDetailsContainerHost by lazy {
        TaskDetailsContainerHostImpl(
            backgroundDispatcher = backgroundDispatcher,
            taskDetails = taskDetails,
            scope = viewModelScope
        )
    }
}
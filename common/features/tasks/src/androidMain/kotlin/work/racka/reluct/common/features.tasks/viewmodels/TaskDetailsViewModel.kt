package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHost
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHostImpl
import work.racka.reluct.common.features.tasks.task_details.repository.TaskDetailsRepository

actual class TaskDetailsViewModel(
    taskDetails: TaskDetailsRepository
) : ViewModel() {
    val host: TaskDetailsContainerHost by lazy {
        TaskDetailsContainerHostImpl(
            taskDetails = taskDetails,
            scope = viewModelScope
        )
    }
}
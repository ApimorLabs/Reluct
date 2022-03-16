package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import work.racka.reluct.common.features.tasks.task_details.TaskDetails
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHost
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHostImpl

actual class TaskDetailsViewModel(
    taskDetails: TaskDetails,
    backgroundDispatcher: CoroutineDispatcher
) : ViewModel() {
    val host: TaskDetailsContainerHost = TaskDetailsContainerHostImpl(
        backgroundDispatcher = backgroundDispatcher,
        taskDetails = taskDetails,
        scope = viewModelScope
    )
}
package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHost
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHostImpl
import work.racka.reluct.common.features.tasks.task_details.repository.TaskDetailsRepository

actual class TaskDetailsViewModel(
    taskDetails: TaskDetailsRepository,
    scope: CoroutineScope
) {
    val host: TaskDetailsContainerHost by lazy {
        TaskDetailsContainerHostImpl(
            taskDetails = taskDetails,
            scope = scope
        )
    }
}
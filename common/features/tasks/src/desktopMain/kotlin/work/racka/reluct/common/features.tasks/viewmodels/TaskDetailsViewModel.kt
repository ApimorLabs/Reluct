package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHost
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHostImpl
import work.racka.reluct.common.features.tasks.task_details.repository.TaskDetailsRepository

actual class TaskDetailsViewModel(
    taskDetails: TaskDetailsRepository,
    backgroundDispatcher: CoroutineDispatcher,
    scope: CoroutineScope
) {
    val host: TaskDetailsContainerHost by lazy {
        TaskDetailsContainerHostImpl(
            backgroundDispatcher = backgroundDispatcher,
            taskDetails = taskDetails,
            scope = scope
        )
    }
}
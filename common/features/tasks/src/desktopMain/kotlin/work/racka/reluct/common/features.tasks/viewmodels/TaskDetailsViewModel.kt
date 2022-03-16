package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.task_details.TaskDetails
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHost
import work.racka.reluct.common.features.tasks.task_details.container.TaskDetailsContainerHostImpl

actual class TaskDetailsViewModel(
    taskDetails: TaskDetails,
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
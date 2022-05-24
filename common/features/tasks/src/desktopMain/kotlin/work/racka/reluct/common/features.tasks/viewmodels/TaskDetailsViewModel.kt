package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.task_details.TaskDetails
import work.racka.reluct.common.features.tasks.task_details.TaskDetailsImpl

actual class TaskDetailsViewModel internal constructor(
    getTasksUseCase: GetTasksUseCase,
    modifyTasksUsesCase: ModifyTaskUseCase,
    taskId: String?,
    scope: CoroutineScope,
) {
    actual val host: TaskDetails by lazy {
        TaskDetailsImpl(
            getTasksUseCase = getTasksUseCase,
            modifyTasksUsesCase = modifyTasksUsesCase,
            taskId = taskId,
            scope = scope
        )
    }
}
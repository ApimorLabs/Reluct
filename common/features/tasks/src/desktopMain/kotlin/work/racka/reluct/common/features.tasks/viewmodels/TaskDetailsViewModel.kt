package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import work.racka.reluct.common.features.tasks.task_details.TaskDetails

actual class TaskDetailsViewModel(
    taskId: String?,
    scope: CoroutineScope,
) {
    actual val host: TaskDetails by KoinJavaComponent.inject(TaskDetails::class.java) {
        parametersOf(taskId, scope)
    }
}
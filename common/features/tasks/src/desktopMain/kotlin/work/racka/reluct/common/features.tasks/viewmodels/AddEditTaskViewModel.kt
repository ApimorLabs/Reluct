package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTask

actual class AddEditTaskViewModel internal constructor(
    taskId: String?,
    scope: CoroutineScope,
) {
    actual val host: AddEditTask by KoinJavaComponent.inject(AddEditTask::class.java) {
        parametersOf(taskId, scope)
    }
}
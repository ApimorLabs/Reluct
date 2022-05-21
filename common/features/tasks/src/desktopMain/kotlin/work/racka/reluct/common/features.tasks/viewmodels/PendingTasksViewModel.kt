package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasksImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase

actual class PendingTasksViewModel internal constructor(
    getTasksUseCase: GetTasksUseCase,
    modifyTasksUsesCase: ModifyTaskUseCase,
    scope: CoroutineScope,
) {
    actual val host: PendingTasks by lazy {
        PendingTasksImpl(
            getTasksUseCase = getTasksUseCase,
            modifyTasksUsesCase = modifyTasksUsesCase,
            scope = scope
        )
    }
}
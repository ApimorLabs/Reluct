package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasksImpl

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
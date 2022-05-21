package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasksImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase

actual class PendingTasksViewModel internal constructor(
    getTasksUseCase: GetTasksUseCase,
    modifyTasksUsesCase: ModifyTaskUseCase,
) : ViewModel() {
    actual val host: PendingTasks by lazy {
        PendingTasksImpl(
            getTasksUseCase = getTasksUseCase,
            modifyTasksUsesCase = modifyTasksUsesCase,
            scope = viewModelScope
        )
    }
}
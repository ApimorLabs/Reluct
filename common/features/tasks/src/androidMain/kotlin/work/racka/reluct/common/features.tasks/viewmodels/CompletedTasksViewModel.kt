package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasks
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasksImpl

actual class CompletedTasksViewModel internal constructor(
    getTasksUseCase: GetTasksUseCase,
    modifyTasksUsesCase: ModifyTaskUseCase,
) : ViewModel() {

    actual val host: CompletedTasks by lazy {
        CompletedTasksImpl(
            getTasksUseCase = getTasksUseCase,
            modifyTasksUsesCase = modifyTasksUsesCase,
            scope = viewModelScope
        )
    }
}
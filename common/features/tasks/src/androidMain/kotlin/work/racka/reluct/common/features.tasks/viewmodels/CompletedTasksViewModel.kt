package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasks
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasksImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

actual class CompletedTasksViewModel internal constructor(
    getTasksUseCase: GetTasksUseCase,
    modifyTasksUsesCase: ModifyTasksUseCase,
) : ViewModel() {

    private val host: CompletedTasks by lazy {
        CompletedTasksImpl(
            getTasksUseCase = getTasksUseCase,
            modifyTasksUsesCase = modifyTasksUsesCase,
            scope = viewModelScope
        )
    }

    actual val uiState: StateFlow<TasksState> = host.uiState

    actual val events: Flow<TasksSideEffect> = host.events

    actual fun toggleDone(task: Task, isDone: Boolean) = host.toggleDone(task, isDone)

    actual fun navigateToTaskDetails(taskId: String) = host.navigateToTaskDetails(taskId)
}
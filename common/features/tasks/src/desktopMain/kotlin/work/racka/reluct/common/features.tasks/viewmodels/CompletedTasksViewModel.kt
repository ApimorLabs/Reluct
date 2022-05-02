package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasks
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasksImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.CompletedTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

actual class CompletedTasksViewModel internal constructor(
    getTasksUseCase: GetTasksUseCase,
    modifyTasksUsesCase: ModifyTaskUseCase,
    scope: CoroutineScope,
) {
    private val host: CompletedTasks by lazy {
        CompletedTasksImpl(
            getTasksUseCase = getTasksUseCase,
            modifyTasksUsesCase = modifyTasksUsesCase,
            scope = scope
        )
    }

    actual val uiState: StateFlow<CompletedTasksState> = host.uiState

    actual val events: Flow<TasksEvents> = host.events

    actual fun toggleDone(task: Task, isDone: Boolean) = host.toggleDone(task, isDone)

    actual fun navigateToTaskDetails(taskId: String) = host.navigateToTaskDetails(taskId)
}
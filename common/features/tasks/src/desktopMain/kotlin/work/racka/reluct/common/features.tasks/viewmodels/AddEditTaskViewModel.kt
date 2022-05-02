package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTask
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTaskImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.AddEditTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

actual class AddEditTaskViewModel internal constructor(
    modifyTasksUseCase: ModifyTaskUseCase,
    taskId: String?,
    scope: CoroutineScope,
) {
    private val host: AddEditTask by lazy {
        AddEditTaskImpl(
            modifyTaskUseCase = modifyTasksUseCase,
            taskId = taskId,
            scope = scope
        )
    }

    actual val uiState: StateFlow<AddEditTasksState> = host.uiState

    actual val events: Flow<TasksEvents> = host.events

    actual fun getTask(id: String?) = host.getTask(id)

    actual fun saveTask(task: EditTask) = host.saveTask(task)

    actual fun goBack() = host.goBack()
}
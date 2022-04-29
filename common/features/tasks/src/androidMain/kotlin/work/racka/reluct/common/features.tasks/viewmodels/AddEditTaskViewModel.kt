package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTask
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTaskImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.AddEditTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

actual class AddEditTaskViewModel internal constructor(
    modifyTasksUseCase: ModifyTasksUseCase,
    getTasksUseCase: GetTasksUseCase,
    taskId: String?,
) : ViewModel() {
    private val host: AddEditTask by lazy {
        AddEditTaskImpl(
            modifyTasksUseCase = modifyTasksUseCase,
            getTasksUseCase = getTasksUseCase,
            taskId = taskId,
            scope = viewModelScope
        )
    }

    actual val uiState: StateFlow<AddEditTasksState> = host.uiState

    actual val events: Flow<TasksEvents> = host.events

    actual fun getTask(id: String?) = host.getTask(id)

    actual fun saveTask(task: EditTask) = host.saveTask(task)

    actual fun goBack() = host.goBack()
}
package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.tasks.add_edit_task.container.AddEditTask
import work.racka.reluct.common.features.tasks.add_edit_task.container.AddEditTaskImpl
import work.racka.reluct.common.features.tasks.add_edit_task.repository.AddEditTaskRepository
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

actual class AddEditTaskViewModel(
    addEditTask: AddEditTaskRepository,
    taskId: String?,
) : ViewModel() {
    private val host: AddEditTask by lazy {
        AddEditTaskImpl(
            addEditTask = addEditTask,
            taskId = taskId,
            scope = viewModelScope
        )
    }

    actual val uiState: StateFlow<TasksState> = host.uiState

    actual val events: Flow<TasksSideEffect> = host.events

    actual fun getTask() = host.getTask()

    actual fun saveTask(task: EditTask) = host.saveTask(task)

    actual fun goBack() = host.goBack()
}
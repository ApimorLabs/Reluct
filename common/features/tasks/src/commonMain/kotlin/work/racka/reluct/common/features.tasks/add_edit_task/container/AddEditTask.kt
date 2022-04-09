package work.racka.reluct.common.features.tasks.add_edit_task.container

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

internal interface AddEditTask {
    val uiState: StateFlow<TasksState>
    val events: Flow<TasksSideEffect>
    fun getTask()
    fun saveTask(task: EditTask)
    fun goBack()
}
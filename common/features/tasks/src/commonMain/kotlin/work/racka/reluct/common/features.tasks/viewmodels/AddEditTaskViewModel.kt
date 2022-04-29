package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.AddEditTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents

expect class AddEditTaskViewModel {
    val uiState: StateFlow<AddEditTasksState>
    val events: Flow<TasksEvents>
    fun getTask(id: String?)
    fun saveTask(task: EditTask)
    fun goBack()
}
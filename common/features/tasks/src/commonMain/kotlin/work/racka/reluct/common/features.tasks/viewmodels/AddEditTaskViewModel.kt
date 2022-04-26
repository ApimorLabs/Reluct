package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

expect class AddEditTaskViewModel {
    val uiState: StateFlow<TasksState>
    val events: Flow<TasksSideEffect>
    fun getTask(id: String?)
    fun saveTask(task: EditTask)
    fun goBack()
}
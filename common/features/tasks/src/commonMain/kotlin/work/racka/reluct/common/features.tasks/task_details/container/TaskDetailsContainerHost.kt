package work.racka.reluct.common.features.tasks.task_details.container

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState

interface TaskDetailsContainerHost {
    val uiState: StateFlow<TasksState>
    val sideEffect: Flow<TasksSideEffect>
    fun getTask(taskId: Long)
    fun deleteTask(taskId: Long)
    fun toggleDone(taskId: Long, isDone: Boolean)
    fun editTask(taskId: Long)
    fun goBack()
}
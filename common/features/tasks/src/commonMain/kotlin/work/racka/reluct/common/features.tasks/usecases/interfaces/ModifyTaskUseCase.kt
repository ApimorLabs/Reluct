package work.racka.reluct.common.features.tasks.usecases.interfaces

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.Task

internal interface ModifyTaskUseCase {
    fun getTaskToEdit(taskId: String): Flow<EditTask?>
    suspend fun saveTask(task: EditTask)
    suspend fun deleteTask(taskId: String)
    fun toggleTaskDone(task: Task, isDone: Boolean)
}
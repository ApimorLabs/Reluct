package work.racka.reluct.common.domain.usecases.tasks

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.Task

interface ModifyTaskUseCase {
    fun getTaskToEdit(taskId: String): Flow<EditTask?>
    suspend fun saveTask(task: EditTask)
    suspend fun deleteTask(taskId: String)
    suspend fun toggleTaskDone(task: Task, isDone: Boolean)
}
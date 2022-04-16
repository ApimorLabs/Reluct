package work.racka.reluct.common.features.tasks.task_details.repository

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

interface TaskDetailsRepository {
    fun getTask(taskId: String): Flow<Task?>
    suspend fun deleteTask(taskId: String)
    fun toggleTask(taskId: String, isDone: Boolean)
}
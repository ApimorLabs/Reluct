package work.racka.reluct.common.features.tasks.task_details.repository

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

interface TaskDetailsRepository {
    suspend fun getTask(taskId: Long): Flow<Task?>
    suspend fun deleteTask(taskId: Long)
    suspend fun toggleTask(taskId: Long, isDone: Boolean)
}
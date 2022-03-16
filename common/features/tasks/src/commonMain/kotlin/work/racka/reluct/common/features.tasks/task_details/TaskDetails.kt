package work.racka.reluct.common.features.tasks.task_details

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

interface TaskDetails {
    suspend fun getTask(taskId: Long): Flow<Task?>
    suspend fun deleteTask(taskId: Long)
    suspend fun toggleTask(taskId: Long, isDone: Boolean)
}
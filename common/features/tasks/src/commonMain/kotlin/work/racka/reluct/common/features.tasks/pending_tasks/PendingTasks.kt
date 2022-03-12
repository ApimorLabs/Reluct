package work.racka.reluct.common.features.tasks.pending_tasks

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

interface PendingTasks {
    suspend fun getTasks(): Flow<Map<String, List<Task>>>
    suspend fun getOverdueTasks(): Flow<List<Task>>
    suspend fun markAsDone(taskId: Long)
}
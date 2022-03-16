package work.racka.reluct.common.features.tasks.completed_tasks

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

interface CompletedTasks {
    fun getTasks(): Flow<List<Task>>
    suspend fun toggleTaskDone(taskId: Long, isDone: Boolean)
}
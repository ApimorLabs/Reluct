package work.racka.reluct.common.features.tasks.pending_tasks.repository

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

interface PendingTasksRepository {
    fun getTasks(): Flow<List<Task>>
    fun toggleTaskDone(task: Task, isDone: Boolean)
}
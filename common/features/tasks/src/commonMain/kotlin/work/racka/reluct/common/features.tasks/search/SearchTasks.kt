package work.racka.reluct.common.features.tasks.search

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

interface SearchTasks {
    suspend fun search(query: String): Flow<List<Task>>
    suspend fun toggleDone(taskId: Long, isDone: Boolean)
}
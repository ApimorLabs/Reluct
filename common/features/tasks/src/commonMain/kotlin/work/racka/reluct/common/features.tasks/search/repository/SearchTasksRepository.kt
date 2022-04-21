package work.racka.reluct.common.features.tasks.search.repository

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

interface SearchTasksRepository {
    fun search(query: String): Flow<List<Task>>
    fun toggleDone(task: Task, isDone: Boolean)
}
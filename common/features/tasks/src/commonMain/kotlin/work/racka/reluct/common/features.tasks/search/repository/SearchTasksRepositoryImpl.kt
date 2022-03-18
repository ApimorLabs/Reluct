package work.racka.reluct.common.features.tasks.search.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.model.domain.tasks.Task

class SearchTasksRepositoryImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher
) : SearchTasksRepository {

    override suspend fun toggleDone(taskId: Long, isDone: Boolean) =
        withContext(backgroundDispatcher) {
            dao.toggleTaskDone(taskId, isDone)
        }

    override suspend fun search(query: String): Flow<List<Task>> =
        withContext(backgroundDispatcher) {
            dao.searchTasks(query)
                .map { list ->
                    val newList = mutableListOf<Task>()
                    list.forEach { taskDbObject ->
                        newList.add(taskDbObject.asTask())
                    }
                    newList.toList()
                }
        }
}
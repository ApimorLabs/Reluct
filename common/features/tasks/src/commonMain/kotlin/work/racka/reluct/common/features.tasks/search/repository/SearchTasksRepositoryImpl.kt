package work.racka.reluct.common.features.tasks.search.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.model.domain.tasks.Task

class SearchTasksRepositoryImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher
) : SearchTasksRepository {

    override fun toggleDone(task: Task, isDone: Boolean) =
        dao.toggleTaskDone(task.id, isDone, task.overdue)

    override fun search(query: String): Flow<List<Task>> =
        dao.searchTasks(query)
            .map { list -> list.map { it.asTask() } }
            .flowOn(backgroundDispatcher)
}
package work.racka.reluct.common.features.tasks.pending_tasks.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.model.domain.tasks.Task

internal class PendingTasksRepositoryImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher
) : PendingTasksRepository {

    override fun getTasks(): Flow<List<Task>> =
        dao.getPendingTasks()
            .map { list -> list.map { it.asTask() } }
            .flowOn(backgroundDispatcher)

    override fun toggleTaskDone(taskId: Long, isDone: Boolean) =
        dao.toggleTaskDone(taskId, isDone)
}
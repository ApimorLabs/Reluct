package work.racka.reluct.common.features.tasks.completed_tasks.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.model.domain.tasks.Task

internal class CompletedTasksRepositoryImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher
) : CompletedTasksRepository {
    override suspend fun getTasks(): Flow<List<Task>> =
        withContext(backgroundDispatcher) {
            dao.getCompletedTasks()
                .map { list -> list.map { it.asTask() } }
        }

    override suspend fun toggleTaskDone(taskId: Long, isDone: Boolean) =
        withContext(backgroundDispatcher) {
            dao.toggleTaskDone(taskId, isDone)
        }
}
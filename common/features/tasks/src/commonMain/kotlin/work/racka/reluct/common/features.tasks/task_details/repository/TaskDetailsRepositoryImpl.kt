package work.racka.reluct.common.features.tasks.task_details.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.model.domain.tasks.Task

internal class TaskDetailsRepositoryImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher
) : TaskDetailsRepository {

    override suspend fun deleteTask(taskId: Long) =
        withContext(backgroundDispatcher) {
            dao.deleteTask(taskId)
        }

    override suspend fun toggleTask(taskId: Long, isDone: Boolean) =
        withContext(backgroundDispatcher) {
            dao.toggleTaskDone(taskId, isDone)
        }

    override suspend fun getTask(taskId: Long): Flow<Task?> =
        withContext(backgroundDispatcher) {
            dao.getTask(taskId).map { taskDbObject ->
                taskDbObject?.asTask()
            }
        }
}
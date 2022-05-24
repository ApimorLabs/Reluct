package work.racka.reluct.common.data.usecases.tasks.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import work.racka.reluct.common.data.mappers.tasks.asTask
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.model.domain.tasks.Task

internal class GetTasksUseCaseImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) : GetTasksUseCase {
    override fun getPendingTasks(factor: Long, limitBy: Long): Flow<List<Task>> =
        dao.getPendingTasks(factor, limitBy)
            .map { list -> list.map { it.asTask() } }
            .flowOn(backgroundDispatcher)

    override fun getCompletedTasks(factor: Long, limitBy: Long): Flow<List<Task>> =
        dao.getCompletedTasks(factor, limitBy)
            .map { list -> list.map { it.asTask() } }
            .flowOn(backgroundDispatcher)

    override fun getSearchedTasks(query: String, factor: Long, limitBy: Long): Flow<List<Task>> =
        dao.searchTasks(query, factor, limitBy)
            .map { list -> list.map { it.asTask() } }
            .flowOn(backgroundDispatcher)

    override fun getTask(taskId: String): Flow<Task?> =
        dao.getTask(taskId).map { taskDbObject ->
            taskDbObject?.asTask()
        }.flowOn(backgroundDispatcher)
}
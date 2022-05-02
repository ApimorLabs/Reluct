package work.racka.reluct.common.features.tasks.usecases.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.model.domain.tasks.Task

internal class GetTasksUseCaseImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) : GetTasksUseCase {
    override fun getPendingTasks(): Flow<List<Task>> =
        dao.getPendingTasks()
            .map { list -> list.map { it.asTask() } }
            .flowOn(backgroundDispatcher)

    override fun getCompletedTasks(): Flow<List<Task>> =
        dao.getCompletedTasks()
            .map { list -> list.map { it.asTask() } }
            .flowOn(backgroundDispatcher)

    override fun getSearchedTasks(query: String): Flow<List<Task>> =
        dao.searchTasks(query)
            .map { list -> list.map { it.asTask() } }
            .flowOn(backgroundDispatcher)

    override fun getTask(taskId: String): Flow<Task?> =
        dao.getTask(taskId).map { taskDbObject ->
            taskDbObject?.asTask()
        }.flowOn(backgroundDispatcher)
}
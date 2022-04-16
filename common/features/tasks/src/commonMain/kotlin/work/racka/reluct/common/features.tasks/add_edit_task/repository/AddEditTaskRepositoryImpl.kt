package work.racka.reluct.common.features.tasks.add_edit_task.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.util.DataMappers.asDatabaseModel
import work.racka.reluct.common.features.tasks.util.DataMappers.asEditTask
import work.racka.reluct.common.model.data.local.task.TaskDbObject
import work.racka.reluct.common.model.domain.tasks.EditTask

internal class AddEditTaskRepositoryImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher
) : AddEditTaskRepository {
    override suspend fun addTask(task: EditTask) =
        withContext(backgroundDispatcher) {
            dao.insertTask(task.asDatabaseModel())
        }

    override fun getTaskToEdit(taskId: String): Flow<EditTask?> =
        dao.getTask(taskId)
            .map { value: TaskDbObject? ->
                value?.asEditTask()
            }.flowOn(backgroundDispatcher)
}
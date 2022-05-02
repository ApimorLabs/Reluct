package work.racka.reluct.common.features.tasks.usecases.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.util.DataMappers.asDatabaseModel
import work.racka.reluct.common.features.tasks.util.DataMappers.asEditTask
import work.racka.reluct.common.model.data.local.task.TaskDbObject
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.Task

internal class ModifyTaskUseCaseImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) : ModifyTaskUseCase {

    override fun getTaskToEdit(taskId: String): Flow<EditTask?> =
        dao.getTask(taskId)
            .map { value: TaskDbObject? ->
                value?.asEditTask()
            }.flowOn(backgroundDispatcher)
            .take(1)

    override suspend fun saveTask(task: EditTask) =
        withContext(backgroundDispatcher) {
            dao.insertTask(task.asDatabaseModel())
        }

    override suspend fun deleteTask(taskId: String) {
        withContext(backgroundDispatcher) {
            dao.deleteTask(taskId)
        }
    }

    override fun toggleTaskDone(task: Task, isDone: Boolean) {
        dao.toggleTaskDone(task.id, isDone, task.overdue)
    }
}
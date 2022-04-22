package work.racka.reluct.common.features.tasks.usecases.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase
import work.racka.reluct.common.features.tasks.util.DataMappers.asDatabaseModel
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.Task

internal class ModifyTasksUseCaseImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) : ModifyTasksUseCase {
    override suspend fun addTask(task: EditTask) =
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
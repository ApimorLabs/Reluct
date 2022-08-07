package work.racka.reluct.common.data.usecases.tasks.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import work.racka.reluct.common.data.mappers.tasks.asDatabaseModel
import work.racka.reluct.common.data.mappers.tasks.asEditTask
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.model.data.local.task.TaskDbObject
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.util.time.TimeUtils
import work.racka.reluct.common.system_service.haptics.HapticFeedback

internal class ModifyTaskUseCaseImpl(
    private val dao: TasksDao,
    private val haptics: HapticFeedback,
    private val backgroundDispatcher: CoroutineDispatcher,
) : ModifyTaskUseCase {

    override fun getTaskToEdit(taskId: String): Flow<EditTask?> {
        haptics.doubleClick()
        return dao.getTask(taskId)
            .map { value: TaskDbObject? ->
                value?.asEditTask()
            }.flowOn(backgroundDispatcher)
            .take(1)
    }

    override suspend fun saveTask(task: EditTask) =
        withContext(backgroundDispatcher) {
            dao.insertTask(task.asDatabaseModel())
            haptics.spinAndFall()
        }

    override suspend fun deleteTask(taskId: String) {
        withContext(backgroundDispatcher) {
            dao.deleteTask(taskId)
            haptics.cascadeFall()
        }
    }

    override fun toggleTaskDone(task: Task, isDone: Boolean) {
        val completedLocalDateTime = if (isDone) TimeUtils.currentLocalDateTime() else null
        dao.toggleTaskDone(task.id, isDone, task.overdue, completedLocalDateTime)
        haptics.tick()
    }
}
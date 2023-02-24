package work.racka.reluct.common.domain.usecases.tasks.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import kotlinx.datetime.toLocalDateTime
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.domain.mappers.tasks.asDatabaseModel
import work.racka.reluct.common.domain.mappers.tasks.asEditTask
import work.racka.reluct.common.domain.usecases.tasks.ManageTasksAlarms
import work.racka.reluct.common.domain.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.enums.responses.NetworkPushResponse
import work.racka.reluct.common.model.util.time.TimeUtils
import work.racka.reluct.common.network.sync.tasks.TasksUpload
import work.racka.reluct.common.services.haptics.HapticFeedback

internal class ModifyTaskUseCaseImpl(
    private val dao: TasksDao,
    private val tasksUpload: TasksUpload,
    private val haptics: HapticFeedback,
    private val manageTasksAlarms: ManageTasksAlarms,
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

    override suspend fun saveTask(task: EditTask) {
        withContext(backgroundDispatcher) {
            val mapped = task.asDatabaseModel()
            val resp = tasksUpload.uploadTask(mapped)
            if (resp !is NetworkPushResponse.Success) {
                dao.insertTask(mapped)
            }
            haptics.click()
            task.reminderLocalDateTime?.let { dateTimeString ->
                manageTasksAlarms
                    .setAlarm(taskId = task.id, dateTime = dateTimeString.toLocalDateTime())
            }
        }
    }

    override suspend fun deleteTask(taskId: String) {
        withContext(backgroundDispatcher) {
            val resp = tasksUpload.deleteTask(taskId)
            if (resp !is NetworkPushResponse.Success) {
                dao.deleteTask(taskId)
            }
            haptics.heavyClick()
            manageTasksAlarms.removeAlarm(taskId)
        }
    }

    // TODO: Implement in FB
    override suspend fun toggleTaskDone(task: Task, isDone: Boolean) {
        withContext(backgroundDispatcher) {
            val completedLocalDateTime = if (isDone) TimeUtils.currentLocalDateTime() else null
            dao.toggleTaskDone(task.id, isDone, task.overdue, completedLocalDateTime)
            haptics.tick()
            if (isDone) {
                manageTasksAlarms.removeAlarm(task.id)
            } else {
                task.reminderDateAndTime?.let { dateTimeString ->
                    manageTasksAlarms
                        .setAlarm(taskId = task.id, dateTime = dateTimeString.toLocalDateTime())
                }
            }
        }
    }
}

package work.racka.reluct.common.features.tasks.task_details

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.util.TasksHelper
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.util.time.TimeUtils

internal class TaskDetailsImpl(
    private val dao: TasksDao
) : TaskDetails {

    override suspend fun deleteTask(taskId: Long) = dao.deleteTask(taskId)

    override suspend fun toggleTask(taskId: Long, isDone: Boolean) =
        dao.toggleTaskDone(taskId, isDone)

    override suspend fun getTask(taskId: Long): Flow<Task?> =
        dao.getTask(taskId).map { taskDbObject ->
            if (taskDbObject == null) {
                null
            } else {
                Task(
                    id = taskDbObject.id,
                    title = taskDbObject.title,
                    description = taskDbObject.description ?: "No Description",
                    done = taskDbObject.done,
                    overdue = TimeUtils.isDateTimeOverdue(
                        taskDbObject.dueDateLocalDateTime,
                        taskDbObject.timeZoneId
                    ),
                    dueDate = TimeUtils.getFormattedDateString(
                        dateTime = taskDbObject.dueDateLocalDateTime,
                        originalTimeZoneId = taskDbObject.timeZoneId
                    ),
                    dueTime = TimeUtils.getTimeFromLocalDateTime(
                        dateTime = taskDbObject.dueDateLocalDateTime,
                        originalTimeZoneId = taskDbObject.timeZoneId
                    ),
                    timeLeftLabel = TimeUtils.getTimeLeftFromLocalDateTime(
                        dateTime = taskDbObject.dueDateLocalDateTime,
                        originalTimeZoneId = taskDbObject.timeZoneId
                    ),
                    reminder = TasksHelper.getReminderDateTime(
                        taskDbObject.reminderLocalDateTime,
                        taskDbObject.timeZoneId
                    )
                )
            }
        }
}
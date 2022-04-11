package work.racka.reluct.common.features.tasks.util

import work.racka.reluct.common.model.data.local.task.TaskDbObject
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.util.time.TimeUtils

internal object DataMappers {
    // Convert from EditTask to database model
    fun EditTask.asDatabaseModel(): TaskDbObject =
        TaskDbObject(
            id = this.id,
            title = this.title,
            description = this.description,
            done = this.done,
            overdue = this.overdue,
            dueDateLocalDateTime = this.dueDateLocalDateTime,
            completedLocalDateTime = this.completedLocalDateTime,
            reminderLocalDateTime = this.reminderLocalDateTime,
            timeZoneId = this.timeZoneId
        )

    // Convert a list of Task database objects to a domain model of Task

    // Convert a Task database object to a domain EditTask model
    fun TaskDbObject.asEditTask(): EditTask =
        EditTask(
            id = this.id,
            title = this.title,
            description = this.description,
            done = this.done,
            overdue = this.overdue,
            dueDateLocalDateTime = this.dueDateLocalDateTime,
            completedLocalDateTime = this.completedLocalDateTime,
            reminderLocalDateTime = this.reminderLocalDateTime,
            timeZoneId = this.timeZoneId
        )

    /**
     * Convert a Task database object to a domain Task model
     *
     * If showShortIntervalAsDay is true then it will show Today, Yesterday, Tomorrow
     * or a day instead of the full date when the interval is within a 3 days difference.
     * If false it will show the full date even if the interval is within a 3 days difference
     */
    fun TaskDbObject.asTask(showShortIntervalAsDay: Boolean = true): Task =
        Task(
            id = this.id,
            title = this.title,
            description = this.description ?: "No Description",
            done = this.done,
            overdue = TimeUtils.isDateTimeOverdue(
                this.dueDateLocalDateTime,
                this.timeZoneId
            ),
            dueDate = TimeUtils.getFormattedDateString(
                dateTime = this.dueDateLocalDateTime,
                originalTimeZoneId = this.timeZoneId,
                showShortIntervalAsDay = showShortIntervalAsDay
            ),
            dueTime = TimeUtils.getTimeFromLocalDateTime(
                dateTime = this.dueDateLocalDateTime,
                originalTimeZoneId = this.timeZoneId
            ),
            timeLeftLabel = TimeUtils.getTimeLeftFromLocalDateTime(
                dateTime = this.dueDateLocalDateTime,
                originalTimeZoneId = this.timeZoneId
            ),
            reminder = TasksHelper.getReminderDateTime(
                this.reminderLocalDateTime,
                this.timeZoneId
            )
        )
}
package work.racka.reluct.common.domain.mappers.tasks

import kotlinx.collections.immutable.toImmutableList
import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.util.time.TimeUtils

/**
 * Convert a Task database object to a domain Task model
 *
 * If showShortIntervalAsDay is true then it will show Today, Yesterday, Tomorrow
 * or a day instead of the full date when the interval is within a 3 days difference.
 * If false it will show the full date even if the interval is within a 3 days difference
 */
fun TaskDbObject.asTask(showShortIntervalAsDay: Boolean = true): Task {
    val completedDateAndTime = if (this.completedLocalDateTime != null) {
        TimeUtils.getDateAndTime(
            localDateTime = this.completedLocalDateTime!!,
            showShortIntervalAsDay = showShortIntervalAsDay,
            timeZoneId = this.timeZoneId
        )
    } else {
        "--/--"
    }

    return Task(
        id = this.id,
        title = this.title,
        description = this.description ?: "No Description",
        done = this.done,
        overdue = if (this.done) {
            this.overdue
        } else {
            TimeUtils.isDateTimeOverdue(
                this.dueDateLocalDateTime,
                this.timeZoneId
            )
        },
        taskLabels = taskLabels.map { it.asTaskLabel() }.toImmutableList(),
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
        reminderFormatted = getReminderDateTime(
            this.reminderLocalDateTime,
            this.timeZoneId
        ),
        reminderDateAndTime = this.reminderLocalDateTime,
        completedDateAndTime = completedDateAndTime
    )
}

private fun getReminderDateTime(
    dateTime: String?,
    originalTimeZoneId: String,
): String {
    dateTime?.let {
        val time = TimeUtils.getTimeFromLocalDateTime(dateTime, originalTimeZoneId)
        val formattedDate = TimeUtils.getFormattedDateString(
            dateTime = dateTime,
            originalTimeZoneId = originalTimeZoneId,
            showShortIntervalAsDay = false
        )
        return "$time - $formattedDate"
    }
    return "No Reminder Set"
}

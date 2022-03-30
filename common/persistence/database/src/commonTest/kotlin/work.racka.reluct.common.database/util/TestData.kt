package work.racka.reluct.common.database.util

import kotlinx.datetime.*
import work.racka.reluct.common.model.data.local.task.TaskDbObject

internal object TestData {
    val taskDbObjects = listOf(
        TaskDbObject(
            id = 1,
            title = "Tasks 1",
            description = null,
            done = false,
            overdue = false,
            dueDateLocalDateTime = forwardTimeBy(months = 2),
            completedLocalDateTime = null,
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        ),
        TaskDbObject(
            id = 2,
            title = "Tasks 2",
            description = null,
            done = false,
            overdue = false,
            dueDateLocalDateTime = forwardTimeBy(months = 3, days = 5),
            completedLocalDateTime = null,
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        ),
        TaskDbObject(
            id = 3,
            title = "Tasks 3",
            description = "Some description",
            done = false,
            overdue = false,
            dueDateLocalDateTime = forwardTimeBy(months = 4, days = 2),
            completedLocalDateTime = null,
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        ),
        TaskDbObject(
            id = 4,
            title = "Tasks 4",
            description = "Some description",
            done = true,
            overdue = false,
            dueDateLocalDateTime = forwardTimeBy(days = -2),
            completedLocalDateTime = forwardTimeBy(days = -2),
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        ),
        TaskDbObject(
            id = 5,
            title = "Tasks 5",
            description = "Some description",
            done = true,
            overdue = false,
            dueDateLocalDateTime = forwardTimeBy(months = -1, days = -2),
            completedLocalDateTime = forwardTimeBy(months = -1, days = -3),
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        )
    )

    private fun forwardTimeBy(years: Int = 0, months: Int = 0, days: Int = 0): String {
        val dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val date = LocalDate(dateTime.year, dateTime.monthNumber, dateTime.dayOfMonth)
        val datePeriod = DatePeriod(years, months, days)
        val newDate = date.plus(datePeriod)
        return LocalDateTime(
            newDate.year,
            newDate.monthNumber,
            newDate.dayOfMonth,
            dateTime.hour,
            dateTime.minute
        )
            .toString()
    }
}
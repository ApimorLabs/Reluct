package work.racka.reluct.common.features.tasks.util

import work.racka.reluct.common.model.util.time.TimeUtils

internal object TasksHelper {
    fun getReminderDateTime(
        dateTime: String?,
        originalTimeZoneId: String
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
}
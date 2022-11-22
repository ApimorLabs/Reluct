package work.racka.reluct.common.domain.usecases.tasks

import kotlinx.datetime.LocalDateTime

internal class DesktopManageTasksAlarms : ManageTasksAlarms {
    override suspend fun setAlarm(taskId: String, dateTime: LocalDateTime) {
        // Not implemented yet
    }

    override suspend fun removeAlarm(taskId: String) {
        // Not Implemented yet
    }

    override suspend fun rescheduleAlarms() {
        // Not Implemented yet
    }
}

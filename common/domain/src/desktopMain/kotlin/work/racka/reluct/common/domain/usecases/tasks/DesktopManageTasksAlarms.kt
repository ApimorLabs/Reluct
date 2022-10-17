package work.racka.reluct.common.domain.usecases.tasks

import kotlinx.datetime.LocalDateTime

internal class DesktopManageTasksAlarms : ManageTasksAlarms {
    override suspend fun setAlarm(taskId: String, dateTime: LocalDateTime) {}

    override suspend fun removeAlarm(taskId: String) {}

    override suspend fun rescheduleAlarms() {}
}
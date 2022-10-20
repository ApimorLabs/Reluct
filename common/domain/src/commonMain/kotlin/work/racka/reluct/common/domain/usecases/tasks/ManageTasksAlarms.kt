package work.racka.reluct.common.domain.usecases.tasks

import kotlinx.datetime.LocalDateTime

interface ManageTasksAlarms {
    suspend fun setAlarm(taskId: String, dateTime: LocalDateTime)
    suspend fun removeAlarm(taskId: String)
    suspend fun rescheduleAlarms()
}
package work.racka.reluct.common.features.tasks.add_edit_task

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.util.UUIDGen
import work.racka.reluct.common.model.util.time.TimeUtils.plus

object DefaultTasks {
    fun emptyEditTask(): EditTask {
        val advancedTime = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .plus(hours = 1)

        return EditTask(
            id = UUIDGen.getString(),
            title = "",
            description = "",
            done = false,
            overdue = false,
            dueDateLocalDateTime = advancedTime.toString(),
            completedLocalDateTime = null,
            reminderLocalDateTime = null,
            timeZoneId = TimeZone.currentSystemDefault().id
        )
    }
}
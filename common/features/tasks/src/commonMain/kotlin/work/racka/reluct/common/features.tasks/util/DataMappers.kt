package work.racka.reluct.common.features.tasks.util

import work.racka.reluct.common.model.data.local.task.TaskDbObject
import work.racka.reluct.common.model.domain.tasks.EditTask

internal object DataMappers {
    // Convert from EditTask to database model
    fun List<EditTask>.asDatabaseModel(): List<TaskDbObject> =
        map { task ->
            TaskDbObject(
                id = task.id,
                title = task.title,
                description = task.description,
                done = task.done,
                overdue = task.overdue,
                dueDateLocalDateTime = task.dueDateLocalDateTime,
                completedLocalDateTime = task.completedLocalDateTime,
                reminderLocalDateTime = task.reminderLocalDateTime,
                timeZoneId = task.timeZoneId
            )
        }

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

    // Convert a Task database object to a domain Task model
}
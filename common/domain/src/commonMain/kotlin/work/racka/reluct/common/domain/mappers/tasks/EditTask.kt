package work.racka.reluct.common.domain.mappers.tasks

import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.model.domain.tasks.EditTask

/**
 *  Convert from EditTask to database model
 */
fun EditTask.asDatabaseModel(): TaskDbObject =
    TaskDbObject(
        id = this.id,
        title = this.title,
        description = this.description,
        done = this.done,
        overdue = this.overdue,
        taskLabels = taskLabels.map { it.asTaskLabelDbObject() },
        dueDateLocalDateTime = this.dueDateLocalDateTime,
        completedLocalDateTime = this.completedLocalDateTime,
        reminderLocalDateTime = this.reminderLocalDateTime,
        timeZoneId = this.timeZoneId
    )

// Convert a list of Task database objects to a domain model of Task

/**
 *  Convert a Task database object to a domain EditTask model
 */
fun TaskDbObject.asEditTask(): EditTask =
    EditTask(
        id = this.id,
        title = this.title,
        description = this.description,
        done = this.done,
        overdue = this.overdue,
        taskLabels = taskLabels.map { it.asTaskLabel() },
        dueDateLocalDateTime = this.dueDateLocalDateTime,
        completedLocalDateTime = this.completedLocalDateTime,
        reminderLocalDateTime = this.reminderLocalDateTime,
        timeZoneId = this.timeZoneId
    )
package work.racka.reluct.common.database.dao.tasks

import work.racka.reluct.common.model.data.local.task.TaskDbObject
import workrackathinkrchivev2commondatabase.db.TasksTable
import workrackathinkrchivev2commondatabase.db.TasksTableQueries

internal object TasksHelpers {

    fun TasksTableQueries.insertTaskToDb(task: TaskDbObject) {
        this.transaction {
            insertTask(
                TasksTable(
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
            )
        }
    }

    fun TasksTableQueries.getAllTasksFromDb() =
        this.getAllTasks(
            mapper = {
                    id, title, description, done, overdue, dueDateLocalDateTime,
                    completedLocalDateTime, reminderLocalDateTime, timeZoneId,
                ->
                taskDbObjectMapper(
                    id, title, description, done, overdue, dueDateLocalDateTime,
                    completedLocalDateTime, reminderLocalDateTime, timeZoneId
                )
            }
        )

    fun TasksTableQueries.getPendingTasksFromDb() =
        this.getPendingTasks(
            mapper = {
                    id, title, description, done, overdue, dueDateLocalDateTime,
                    completedLocalDateTime, reminderLocalDateTime, timeZoneId,
                ->
                taskDbObjectMapper(
                    id, title, description, done, overdue, dueDateLocalDateTime,
                    completedLocalDateTime, reminderLocalDateTime, timeZoneId
                )
            }
        )

    fun TasksTableQueries.getCompletedTasksFromDb() =
        this.getCompeletedTasks(
            mapper = {
                    id, title, description, done, overdue, dueDateLocalDateTime,
                    completedLocalDateTime, reminderLocalDateTime, timeZoneId,
                ->
                taskDbObjectMapper(
                    id, title, description, done, overdue, dueDateLocalDateTime,
                    completedLocalDateTime, reminderLocalDateTime, timeZoneId
                )
            }
        )

    fun TasksTableQueries.getTasksBetweenDateTimeStringsFromDb(
        startLocalDateTime: String,
        endLocalDateTime: String,
    ) = this.getTasksBetweenDateTimeStrings(
        startLocalDateTime = startLocalDateTime,
        endLocalDateTime = endLocalDateTime,
        mapper = {
                id, title, description, done, overdue, dueDateLocalDateTime,
                completedLocalDateTime, reminderLocalDateTime, timeZoneId,
            ->
            taskDbObjectMapper(
                id, title, description, done, overdue, dueDateLocalDateTime,
                completedLocalDateTime, reminderLocalDateTime, timeZoneId
            )
        }
    )

    fun TasksTableQueries.getTaskFromDb(taskId: String) =
        this.getTask(
            id = taskId,
            mapper = {
                    id, title, description, done, overdue, dueDateLocalDateTime,
                    completedLocalDateTime, reminderLocalDateTime, timeZoneId,
                ->
                taskDbObjectMapper(
                    id, title, description, done, overdue, dueDateLocalDateTime,
                    completedLocalDateTime, reminderLocalDateTime, timeZoneId
                )
            }
        )

    fun TasksTableQueries.searchTasksFromDb(query: String) =
        this.searchTasks(
            query = query,
            mapper = {
                    id, title, description, done, overdue, dueDateLocalDateTime,
                    completedLocalDateTime, reminderLocalDateTime, timeZoneId,
                ->
                taskDbObjectMapper(
                    id, title, description, done, overdue, dueDateLocalDateTime,
                    completedLocalDateTime, reminderLocalDateTime, timeZoneId
                )
            }
        )


    private fun taskDbObjectMapper(
        id: String, title: String, description: String?, done: Boolean, overdue: Boolean,
        dueDateLocalDateTime: String, completedLocalDateTime: String?,
        reminderLocalDateTime: String?, timeZoneId: String,
    ): TaskDbObject {
        return TaskDbObject(
            id = id,
            title = title,
            description = description,
            done = done,
            overdue = overdue,
            dueDateLocalDateTime = dueDateLocalDateTime,
            completedLocalDateTime = completedLocalDateTime,
            reminderLocalDateTime = reminderLocalDateTime,
            timeZoneId = timeZoneId
        )
    }
}
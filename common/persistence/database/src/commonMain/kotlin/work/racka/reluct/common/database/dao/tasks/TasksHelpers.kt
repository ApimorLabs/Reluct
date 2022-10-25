package work.racka.reluct.common.database.dao.tasks

import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.tables.TasksTable
import work.racka.reluct.common.database.tables.TasksTableQueries

internal object TasksHelpers {

    fun TasksTableQueries.insertTaskToDb(task: TaskDbObject) {
        transaction {
            insertTask(
                TasksTable(
                    id = task.id,
                    title = task.title,
                    description = task.description,
                    done = task.done,
                    overdue = task.overdue,
                    taskLabelsId = task.taskLabels.map { it.id },
                    dueDateLocalDateTime = task.dueDateLocalDateTime,
                    completedLocalDateTime = task.completedLocalDateTime,
                    reminderLocalDateTime = task.reminderLocalDateTime,
                    timeZoneId = task.timeZoneId
                )
            )
        }
    }

    fun TasksTableQueries.getAllTasksFromDb() = getAllTasks(mapper = taskDbObjectMapper)

    fun TasksTableQueries.getPendingTasksFromDb(factor: Long, limitBy: Long) =
        getPendingTasks(
            factor = factor,
            limitBy = limitBy,
            mapper = taskDbObjectMapper
        )

    fun TasksTableQueries.getCompletedTasksFromDb(factor: Long, limitBy: Long) =
        getCompeletedTasks(
            factor = factor,
            limitBy = limitBy,
            mapper = taskDbObjectMapper
        )

    fun TasksTableQueries.getTasksBetweenDateTimeStringsFromDb(
        startLocalDateTime: String,
        endLocalDateTime: String,
    ) = getTasksBetweenDateTimeStrings(
        startLocalDateTime = startLocalDateTime,
        endLocalDateTime = endLocalDateTime,
        mapper = taskDbObjectMapper
    )

    fun TasksTableQueries.getTaskFromDb(taskId: String) =
        getTask(id = taskId, mapper = taskDbObjectMapper)

    fun TasksTableQueries.searchTasksFromDb(query: String, factor: Long, limitBy: Long) =
        searchTasks(
            query = query,
            factor = factor,
            limitBy = limitBy,
            mapper = taskDbObjectMapper
        )


    private val taskDbObjectMapper: (
        id: String, title: String, description: String?, done: Boolean, overdue: Boolean,
        dueDateLocalDateTime: String, completedLocalDateTime: String?,
        reminderLocalDateTime: String?, timeZoneId: String, taskLabelsId: List<String>
    ) -> TaskDbObject = {
            id, title, description, done, overdue, dueDateLocalDateTime,
            completedLocalDateTime, reminderLocalDateTime, timeZoneId, taskLabelsId,
        ->
        TaskDbObject(
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
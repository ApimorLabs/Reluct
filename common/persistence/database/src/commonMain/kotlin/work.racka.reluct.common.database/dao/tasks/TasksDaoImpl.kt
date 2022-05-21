package work.racka.reluct.common.database.dao.tasks

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import work.racka.reluct.common.database.dao.DatabaseWrapper
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getAllTasksFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getCompletedTasksFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getPendingTasksFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getTaskFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getTasksBetweenDateTimeStringsFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.insertTaskToDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.searchTasksFromDb
import work.racka.reluct.common.model.data.local.task.TaskDbObject

internal class TasksDaoImpl(
    private val coroutineScope: CoroutineScope,
    databaseWrapper: DatabaseWrapper,
) : TasksDao {

    private val tasksQueries = databaseWrapper.instance?.tasksTableQueries

    override fun insertTask(task: TaskDbObject) {
        tasksQueries?.insertTaskToDb(task)
    }

    override fun getAllTasks(): Flow<List<TaskDbObject>> =
        tasksQueries?.getAllTasksFromDb()
            ?.asFlow()
            ?.mapToList(coroutineScope.coroutineContext)
            ?: flowOf(emptyList())


    override fun getTask(id: String): Flow<TaskDbObject?> =
        tasksQueries?.getTaskFromDb(id)
            ?.asFlow()
            ?.mapToOneOrNull(coroutineScope.coroutineContext)
            ?: flowOf(null)

    override fun searchTasks(query: String, factor: Long, limitBy: Long): Flow<List<TaskDbObject>> =
        tasksQueries?.searchTasksFromDb("%$query%", factor, limitBy)
            ?.asFlow()
            ?.mapToList(coroutineScope.coroutineContext)
            ?: flowOf(emptyList())

    override fun getPendingTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> =
        tasksQueries?.getPendingTasksFromDb(factor, limitBy)
            ?.asFlow()
            ?.mapToList(coroutineScope.coroutineContext)
            ?: flowOf(emptyList())

    override fun getCompletedTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> =
        tasksQueries?.getCompletedTasksFromDb(factor, limitBy)
            ?.asFlow()
            ?.mapToList(coroutineScope.coroutineContext)
            ?: flowOf(emptyList())

    override fun getTasksBetweenDateTime(
        startLocalDateTime: String,
        endLocalDateTime: String,
    ): Flow<List<TaskDbObject>> =
        tasksQueries?.getTasksBetweenDateTimeStringsFromDb(startLocalDateTime, endLocalDateTime)
            ?.asFlow()
            ?.mapToList(coroutineScope.coroutineContext)
            ?: flowOf(emptyList())

    override fun toggleTaskDone(
        id: String,
        isDone: Boolean,
        wasOverDue: Boolean,
        completedLocalDateTime: String?,
    ) {
        tasksQueries?.transaction {
            tasksQueries.toggleTaskDone(
                isDone = isDone,
                wasOverdue = wasOverDue,
                id = id,
                completeLocalDateTime = completedLocalDateTime
            )
        }
    }

    override fun deleteTask(id: String) {
        tasksQueries?.deleteTask(id)
    }

    override fun deleteAllCompletedTasks() {
        tasksQueries?.deleteAllCompletedTasks()
    }

    override fun deleteAll() {
        tasksQueries?.deleteAll()
    }
}
package work.racka.reluct.common.database.dao.tasks

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import work.racka.reluct.common.database.dao.DatabaseWrapper
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getAllTasksFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getCompletedTasksFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getPendingTasksFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getTaskFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.insertTaskToDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.searchTasksFromDb
import work.racka.reluct.common.model.data.local.task.TaskDbObject

internal class TasksDaoImpl(
    private val coroutineScope: CoroutineScope = MainScope(),
    databaseWrapper: DatabaseWrapper
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


    override fun getTask(id: Long): Flow<TaskDbObject?> =
        tasksQueries?.getTaskFromDb(id)
            ?.asFlow()
            ?.mapToOne(coroutineScope.coroutineContext)
            ?: flowOf(null)

    override fun searchTasks(query: String): Flow<List<TaskDbObject>> =
        tasksQueries?.searchTasksFromDb("%$query%")
            ?.asFlow()
            ?.mapToList(coroutineScope.coroutineContext)
            ?: flowOf(emptyList())

    override fun getPendingTasks(): Flow<List<TaskDbObject>> =
        tasksQueries?.getPendingTasksFromDb()
            ?.asFlow()
            ?.mapToList(coroutineScope.coroutineContext)
            ?: flowOf(emptyList())

    override fun getCompletedTasks(): Flow<List<TaskDbObject>> =
        tasksQueries?.getCompletedTasksFromDb()
            ?.asFlow()
            ?.mapToList(coroutineScope.coroutineContext)
            ?: flowOf(emptyList())

    override fun deleteTask(id: Long) {
        tasksQueries?.deleteTask(id)
    }

    override fun deleteAllCompletedTasks() {
        tasksQueries?.deleteAllCompletedTasks()
    }

    override fun deleteAll() {
        tasksQueries?.deleteAll()
    }
}
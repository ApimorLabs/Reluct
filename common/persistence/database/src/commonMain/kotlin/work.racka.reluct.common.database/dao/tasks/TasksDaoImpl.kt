package work.racka.reluct.common.database.dao.tasks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.database.dao.DatabaseWrapper
import work.racka.reluct.common.model.data.local.task.TaskDbObject

internal class TasksDaoImpl(
    private val coroutineScope: CoroutineScope = MainScope(),
    databaseWrapper: DatabaseWrapper
) : TasksDao {

    private val tasksQueries = databaseWrapper.instance?.tasksTableQueries

    override fun insertTask(task: TaskDbObject) {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): Flow<List<TaskDbObject>> {
        TODO("Not yet implemented")
    }

    override fun getTask(id: Long): Flow<TaskDbObject?> {
        TODO("Not yet implemented")
    }

    override fun searchTasks(query: String): Flow<List<TaskDbObject>> {
        TODO("Not yet implemented")
    }

    override fun getPendingTasks(): Flow<List<TaskDbObject>> {
        TODO("Not yet implemented")
    }

    override fun getCompletedTasks(): Flow<List<TaskDbObject>> {
        TODO("Not yet implemented")
    }

    override fun deleteTask(id: Long) {
        TODO("Not yet implemented")
    }

    override fun deleteCompletedTasks() {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }
}
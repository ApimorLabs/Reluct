package work.racka.reluct.common.database.dao.tasks

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.data.local.task.TaskDbObject

interface TasksDao {
    fun insertTask(task: TaskDbObject)
    fun getAllTasks(): Flow<List<TaskDbObject>>
    fun getTask(id: Long): Flow<TaskDbObject?>
    fun searchTasks(query: String): Flow<List<TaskDbObject>>
    fun getPendingTasks(): Flow<List<TaskDbObject>>
    fun getCompletedTasks(): Flow<List<TaskDbObject>>
    fun deleteTask(id: Long)
    fun deleteCompletedTasks()
    fun deleteAll()
}
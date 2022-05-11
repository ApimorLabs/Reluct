package work.racka.reluct.common.database.dao.tasks

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.data.local.task.TaskDbObject

interface TasksDao {
    fun insertTask(task: TaskDbObject)
    fun getAllTasks(): Flow<List<TaskDbObject>>
    fun getTask(id: String): Flow<TaskDbObject?>
    fun searchTasks(query: String): Flow<List<TaskDbObject>>
    fun getPendingTasks(): Flow<List<TaskDbObject>>
    fun getCompletedTasks(): Flow<List<TaskDbObject>>
    fun getTasksBetweenDateTime(
        startLocalDateTime: String,
        endLocalDateTime: String,
    ): Flow<List<TaskDbObject>>

    fun toggleTaskDone(
        id: String,
        isDone: Boolean,
        wasOverDue: Boolean,
        completedLocalDateTime: String?,
    )

    fun deleteTask(id: String)
    fun deleteAllCompletedTasks()
    fun deleteAll()
}
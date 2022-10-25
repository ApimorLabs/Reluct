package work.racka.reluct.common.database.dao.tasks

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.database.models.TaskDbObject

interface TasksDao {
    fun insertTask(task: TaskDbObject)
    fun getAllTasks(): Flow<List<TaskDbObject>>
    fun getTask(id: String): Flow<TaskDbObject?>

    /**
     * [limitBy] is for how much limit is applied in query. Default is 10
     * [limitBy] * [factor] produces required limit applied in query
     **/
    fun searchTasks(query: String, factor: Long, limitBy: Long = 10): Flow<List<TaskDbObject>>

    /**
     * [limitBy] is for how much limit is applied in query. Default is 10
     * [limitBy] * [factor] produces required limit applied in query
     **/
    fun getPendingTasks(factor: Long, limitBy: Long = 10): Flow<List<TaskDbObject>>

    /**
     * [limitBy] is for how much limit is applied in query. Default is 10
     * [limitBy] * [factor] produces required limit applied in query
     **/
    fun getCompletedTasks(factor: Long, limitBy: Long = 10): Flow<List<TaskDbObject>>

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
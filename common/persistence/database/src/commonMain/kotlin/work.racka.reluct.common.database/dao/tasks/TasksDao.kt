package work.racka.reluct.common.database.dao.tasks

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.data.local.task.TaskDbObject
import work.racka.reluct.common.model.domain.tasks.EditTask

interface TasksDao {
    fun insertTask(task: EditTask)
    fun getAllTasks(): Flow<List<TaskDbObject>>
    fun getTask(id: Long): Flow<TaskDbObject?>
    fun searchTasks(query: String): Flow<List<TaskDbObject>>
    fun getPendingTasks(): Flow<List<TaskDbObject>>
    fun getCompletedTasks(): Flow<List<TaskDbObject>>
    fun toggleTaskDone(id: Long, isDone: Boolean)
    fun deleteTask(id: Long)
    fun deleteAllCompletedTasks()
    fun deleteAll()
}
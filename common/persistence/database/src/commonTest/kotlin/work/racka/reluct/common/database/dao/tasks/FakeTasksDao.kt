package work.racka.reluct.common.database.dao.tasks

import kotlinx.coroutines.flow.*
import work.racka.reluct.common.database.util.TasksTestData
import work.racka.reluct.common.model.data.local.task.TaskDbObject

class FakeTasksDao : TasksDao {

    private val tasks = MutableStateFlow(TasksTestData.taskDbObjects)

    override fun insertTask(task: TaskDbObject) {
        tasks.update {
            it.toMutableList().apply {
                add(task)
            }.toList()
        }
    }

    override fun getAllTasks(): Flow<List<TaskDbObject>> = tasks.asStateFlow()

    override fun getTask(id: String): Flow<TaskDbObject?> = tasks.transform { it.firstOrNull() }

    override fun searchTasks(query: String, factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        return tasks.transform { it.take(2) }
    }

    override fun getPendingTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        return tasks.transform { list -> list.filterNot { it.done } }
    }

    override fun getCompletedTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        return tasks.transform { list -> list.filter { it.done } }
    }

    override fun getTasksBetweenDateTime(
        startLocalDateTime: String,
        endLocalDateTime: String
    ): Flow<List<TaskDbObject>> {
        val timeRangeString = startLocalDateTime..endLocalDateTime
        return tasks
            .transform { list -> list.filter { timeRangeString.contains(it.dueDateLocalDateTime) } }
    }

    override fun toggleTaskDone(
        id: String,
        isDone: Boolean,
        wasOverDue: Boolean,
        completedLocalDateTime: String?
    ) {
        tasks.update { list ->
            val newItem = list.first().copy(
                overdue = wasOverDue,
                done = isDone,
                completedLocalDateTime = completedLocalDateTime
            )
            val newList = list.toMutableList()
            newList[0] = newItem
            newList.toList()
        }
    }

    override fun deleteTask(id: String) {
        tasks.update { list ->
            val newList = list.toMutableList()
            newList.removeAt(0)
            newList.toList()
        }
    }

    override fun deleteAllCompletedTasks() {
        tasks.update { list ->
            val newList = list.toMutableList()
            newList.removeAll { it.done }
            newList.toList()
        }
    }

    override fun deleteAll() {
        tasks.update { listOf() }
    }
}
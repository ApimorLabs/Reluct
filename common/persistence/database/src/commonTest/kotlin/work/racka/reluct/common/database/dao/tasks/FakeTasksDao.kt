package work.racka.reluct.common.database.dao.tasks

import kotlinx.coroutines.flow.*
import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.util.TasksTestData

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

    override fun getTask(id: String): Flow<TaskDbObject?> =
        tasks.transform { list -> list.firstOrNull { it.id == id } }

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
            val item = list.firstOrNull { it.id == id }?.copy(
                done = isDone,
                overdue = wasOverDue,
                completedLocalDateTime = completedLocalDateTime
            )
            list.toMutableList().apply {
                item?.let { task ->
                    removeAll { it.id == id }
                    add(task)
                }
            }.toList()
        }
    }

    override fun deleteTask(id: String) {
        tasks.update { list -> list.toMutableList().apply { removeAll { it.id == id } }.toList() }
    }

    override fun deleteAllCompletedTasks() {
        tasks.update { list -> list.toMutableList().apply { removeAll { it.done } }.toList() }
    }

    override fun deleteAll() {
        tasks.update { listOf() }
    }
}
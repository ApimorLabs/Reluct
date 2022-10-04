package work.racka.reluct.common.database.dao.tasks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform
import work.racka.reluct.common.database.util.TestData
import work.racka.reluct.common.model.data.local.task.TaskDbObject

class FakeTasksDao : TasksDao {
    private var allTasks = flowOf(TestData.taskDbObjects)

    override fun insertTask(task: TaskDbObject) {}

    override fun getAllTasks(): Flow<List<TaskDbObject>> = allTasks

    override fun getTask(id: String): Flow<TaskDbObject?> = allTasks.transform { it.first() }

    override fun searchTasks(query: String, factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        return allTasks.transform { it.take(2) }
    }

    override fun getPendingTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        return allTasks.transform { list -> list.filterNot { it.done } }
    }

    override fun getCompletedTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> {
        return allTasks.transform { list -> list.filter { it.done } }
    }

    override fun getTasksBetweenDateTime(
        startLocalDateTime: String,
        endLocalDateTime: String
    ): Flow<List<TaskDbObject>> {
        val timeRangeString = startLocalDateTime..endLocalDateTime
        return allTasks
            .transform { list -> list.filter { timeRangeString.contains(it.dueDateLocalDateTime) } }
    }

    override fun toggleTaskDone(
        id: String,
        isDone: Boolean,
        wasOverDue: Boolean,
        completedLocalDateTime: String?
    ) {
        val newFlow: Flow<List<TaskDbObject>> = allTasks.transform { list ->
            val newItem = list.first().copy(
                overdue = wasOverDue,
                done = isDone,
                completedLocalDateTime = completedLocalDateTime
            )
            val newList = list.toMutableList()
            newList[0] = newItem
            newList.toList()
        }
        allTasks = newFlow
    }

    override fun deleteTask(id: String) {
        val newFlow: Flow<List<TaskDbObject>> = allTasks.transform { list ->
            val newList = list.toMutableList()
            newList.removeAt(0)
            newList.toList()
        }
        allTasks = newFlow
    }

    override fun deleteAllCompletedTasks() {
        val newFlow: Flow<List<TaskDbObject>> = allTasks.transform { list ->
            val newList = list.toMutableList()
            newList.removeAll { it.done }
            newList.toList()
        }
        allTasks = newFlow
    }

    override fun deleteAll() {
        allTasks = flowOf(listOf())
    }
}
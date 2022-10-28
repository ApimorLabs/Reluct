package work.racka.reluct.common.database.dao.tasks

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.DatabaseWrapper
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.asDbObject
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getAllTasksFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getCompletedTasksFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getPendingTasksFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getTaskFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.getTasksBetweenDateTimeStringsFromDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.insertAllLabelsToDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.insertLabelToDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.insertTaskToDb
import work.racka.reluct.common.database.dao.tasks.TasksHelpers.searchTasksFromDb
import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject

internal class TasksDaoImpl(
    private val dispatcher: CoroutineDispatcher,
    databaseWrapper: DatabaseWrapper,
) : TasksDao {

    private val tasksQueries = databaseWrapper.instance?.tasksTableQueries
    private val labelQueries = databaseWrapper.instance?.taskLabelsTableQueries

    override fun insertTask(task: TaskDbObject) {
        tasksQueries?.insertTaskToDb(task)
    }

    override fun getAllTasks(): Flow<List<TaskDbObject>> =
        tasksQueries?.getAllTasksFromDb(onGetLabel = ::getTaskLabelSync)
            ?.asFlow()
            ?.mapToList(dispatcher)
            ?: flowOf(emptyList())

    // TODO: Use later
    fun getTasks(): Flow<List<TaskDbObject>> {
        val dbTasks = tasksQueries?.getAllTasks()?.asFlow()?.mapToList(dispatcher)
            ?: flowOf(emptyList())
        val dbLabels = getAllTaskLabels()
        return dbTasks.zip(dbLabels) { tasks, labels ->
            withContext(dispatcher) {
                tasks.map { task -> task.asDbObject(labels) }
            }
        }
    }


    override fun getTask(id: String): Flow<TaskDbObject?> =
        tasksQueries?.getTaskFromDb(id, onGetLabel = ::getTaskLabelSync)
            ?.asFlow()
            ?.mapToOneOrNull(dispatcher)
            ?: flowOf(null)

    override fun searchTasks(query: String, factor: Long, limitBy: Long): Flow<List<TaskDbObject>> =
        tasksQueries?.searchTasksFromDb(
            query = "%$query%",
            factor = factor,
            limitBy = limitBy,
            onGetLabel = ::getTaskLabelSync
        )
            ?.asFlow()
            ?.mapToList(dispatcher)
            ?: flowOf(emptyList())

    override fun getPendingTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> =
        tasksQueries?.getPendingTasksFromDb(factor, limitBy, onGetLabel = ::getTaskLabelSync)
            ?.asFlow()
            ?.mapToList(dispatcher)
            ?: flowOf(emptyList())

    override fun getCompletedTasks(factor: Long, limitBy: Long): Flow<List<TaskDbObject>> =
        tasksQueries?.getCompletedTasksFromDb(factor, limitBy, onGetLabel = ::getTaskLabelSync)
            ?.asFlow()
            ?.mapToList(dispatcher)
            ?: flowOf(emptyList())

    override fun getTasksBetweenDateTime(
        startLocalDateTime: String,
        endLocalDateTime: String,
    ): Flow<List<TaskDbObject>> =
        tasksQueries?.getTasksBetweenDateTimeStringsFromDb(
            startLocalDateTime = startLocalDateTime,
            endLocalDateTime = endLocalDateTime,
            onGetLabel = ::getTaskLabelSync
        )
            ?.asFlow()
            ?.mapToList(dispatcher)
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

    override fun addAllTaskLabels(labels: List<TaskLabelDbObject>) {
        labelQueries?.insertAllLabelsToDb(labels)
    }

    override fun addTaskLabel(label: TaskLabelDbObject) {
        labelQueries?.insertLabelToDb(label)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllTaskLabels(): Flow<List<TaskLabelDbObject>> =
        labelQueries?.getLables(mapper = TasksHelpers.taskLabelsMapper)?.asFlow()
            ?.mapToList(dispatcher)?.mapLatest { it.asReversed() }
            ?: flowOf(emptyList())

    override fun getTaskLabel(id: String): Flow<TaskLabelDbObject?> =
        labelQueries?.getLabelById(id = id, mapper = TasksHelpers.taskLabelsMapper)?.asFlow()
            ?.mapToOneOrNull(dispatcher) ?: flowOf(null)

    override fun getTaskLabelSync(id: String): TaskLabelDbObject? =
        labelQueries?.getLabelById(id = id, mapper = TasksHelpers.taskLabelsMapper)
            ?.executeAsOneOrNull()

    override fun deleteTaskLabel(id: String) {
        labelQueries?.deleteLabel(id)
    }

    override fun deleteAllTaskLabels() {
        labelQueries?.deleteAll()
    }
}
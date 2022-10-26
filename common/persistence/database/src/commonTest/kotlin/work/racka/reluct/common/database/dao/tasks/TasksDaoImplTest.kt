package work.racka.reluct.common.database.dao.tasks

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import work.racka.reluct.common.database.di.getDatabaseWrapper
import work.racka.reluct.common.database.util.TasksTestData
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class TasksDaoImplTest {

    private lateinit var dao: TasksDao

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        dao = TasksDaoImpl(
            dispatcher = StandardTestDispatcher(),
            databaseWrapper = getDatabaseWrapper()
        )
    }

    @AfterTest
    fun teardown() {
    }

    @Test
    fun readAndWriteToDb_ProvidedATaskDbObject_ShouldReadAllTasksObjectsInDb() =
        runTest {
            val tasks = TasksTestData.taskDbObjects
            tasks.forEach {
                dao.insertTask(it)
            }
            val dbObjects = dao.getAllTasks()
            launch {
                dbObjects.test {
                    val actual = awaitItem()
                    awaitComplete()
                    assertTrue(actual.isNotEmpty())
                    assertTrue(actual.containsAll(tasks))
                }
            }
        }

    @Test
    fun getTask_WhenTaskFoundInDb_ReturnsSingleTaskMatchingTheId() = runTest {
        val tasks = TasksTestData.taskDbObjects
        val expected = tasks.first()
        tasks.forEach {
            dao.insertTask(it)
        }
        val result = dao.getTask(expected.id)
        launch {
            result.test {
                val actual = awaitItem()
                awaitComplete()
                assertNotNull(actual)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun getTask_WhenTaskNotFoundInDb_ReturnsNull() = runTest {
        val tasks = TasksTestData.taskDbObjects
        val wrongId = "20000L"
        tasks.forEach {
            dao.insertTask(it)
        }
        val result = dao.getTask(wrongId)
        launch {
            result.test {
                val actual = awaitItem()
                println(actual)
                assertNull(actual)
                awaitComplete()
            }
        }
    }

    @Test
    fun getPendingTasks_ReturnsAllPendingTasks() = runTest {
        val tasks = TasksTestData.taskDbObjects
        val expectedTasks = tasks.filter { !it.done }
        val factor = 1L
        val limitBy = 50L

        tasks.forEach {
            dao.insertTask(it)
        }
        val result = dao.getPendingTasks(factor, limitBy)
        launch {
            result.test {
                val actual = awaitItem()
                awaitComplete()
                assertTrue(actual.containsAll(expectedTasks))
            }
        }
    }

    @Test
    fun getCompletedTasks_ReturnsAllCompletedTasks() = runTest {
        val tasks = TasksTestData.taskDbObjects
        val expectedTasks = tasks.filter { it.done }
        val factor = 1L
        val limitBy = 50L

        tasks.forEach {
            dao.insertTask(it)
        }
        val result = dao.getCompletedTasks(factor, limitBy)
        launch {
            result.test {
                val actual = awaitItem()
                awaitComplete()
                assertTrue(actual.containsAll(expectedTasks))
            }
        }
    }

    @Test
    fun searchTasks_WhenQueryMatchedTitleOrDesc_ReturnsListOfTasksMatchingQuery() = runTest {
        val tasks = TasksTestData.taskDbObjects
        val titleQuery = "Task 1"
        val descQuery = "Desc4"
        val expectedTasks1 = tasks.filter { it.title.contains(titleQuery) }
        val expectedTasks2 = tasks.filter { it.description?.contains(descQuery) ?: false }
        val factor = 1L
        val limitBy = 50L

        tasks.forEach {
            dao.insertTask(it)
        }
        val result1 = dao.searchTasks(titleQuery, factor, limitBy)
        val result2 = dao.searchTasks(descQuery, factor, limitBy)
        launch {
            result1.test {
                val actual = awaitItem()
                awaitComplete()
                println(actual)
                println(expectedTasks1)
                assertTrue(actual.containsAll(expectedTasks1))
            }

            result2.test {
                val actual = awaitItem()
                awaitComplete()
                println(actual)
                println(expectedTasks2)
                assertTrue(actual.containsAll(expectedTasks2))
            }
        }
    }

    @Test
    fun searchTasks_WhenQueryNotMatchedTitleOrDesc_ReturnsEmptyList() = runTest {
        val tasks = TasksTestData.taskDbObjects
        val query = "Task no where to be found"
        val factor = 1L
        val limitBy = 50L

        tasks.forEach {
            dao.insertTask(it)
        }
        val result = dao.searchTasks(query, factor, limitBy)
        launch {
            result.test {
                val actual = awaitItem()
                awaitComplete()
                println(actual)
                assertTrue(actual.isEmpty())
            }
        }
    }

    @Test
    fun toggleDone_WhenTaskIsDoneIsTrue_MarksTaskAsCompletedInDb() = runTest {
        val tasks = TasksTestData.taskDbObjects
        val tasksNotDone = tasks.filter { !it.done }
        val taskNotDone = tasksNotDone.first()
        val taskDone = taskNotDone.copy(done = true)
        val completedLDT = taskNotDone.dueDateLocalDateTime
        val factor = 1L
        val limitBy = 50L

        tasks.forEach {
            dao.insertTask(it)
        }
        dao.toggleTaskDone(
            id = taskNotDone.id,
            isDone = true,
            wasOverDue = false,
            completedLocalDateTime = completedLDT
        )
        val pendingTasks = dao.getPendingTasks(factor, limitBy)
        val completedTasks = dao.getCompletedTasks(factor, limitBy)
        launch {
            pendingTasks.test {
                val actual = awaitItem()
                awaitComplete()
                assertFalse(actual.contains(taskNotDone))
            }
            completedTasks.test {
                val actual = awaitItem()
                awaitComplete()
                assertTrue(actual.contains(taskDone))
            }
        }
    }

    @Test
    fun deleteTask_RemovesTaskFromDb() = runTest {
        val tasks = TasksTestData.taskDbObjects
        val deletedTask = tasks.first()
        tasks.forEach {
            dao.insertTask(it)
        }
        dao.deleteTask(deletedTask.id)
        val result = dao.getAllTasks()
        launch {
            result.test {
                val actual = awaitItem()
                awaitComplete()
                assertFalse(actual.contains(deletedTask))
            }
        }
    }

    @Test
    fun deleteAllCompleted_RemovesAllCompletedTasksFromDb() = runTest {
        val tasks = TasksTestData.taskDbObjects
        val deletedTasks = tasks.filter { it.done }
        val factor = 1L
        val limitBy = 50L

        tasks.forEach {
            dao.insertTask(it)
        }
        dao.deleteAllCompletedTasks()
        val result = dao.getCompletedTasks(factor, limitBy)
        launch {
            result.test {
                val actual = awaitItem()
                awaitComplete()
                assertFalse(actual.containsAll(deletedTasks))
            }
        }
    }

    @Test
    fun deleteAll_RemovesAllTasksFromDb() = runTest {
        val tasks = TasksTestData.taskDbObjects
        tasks.forEach {
            dao.insertTask(it)
        }
        dao.deleteAll()
        val result = dao.getAllTasks()
        launch {
            result.test {
                val actual = awaitItem()
                assertTrue(actual.isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun addLabels_getAllTaskLabels_ShouldReturnAllInsertedLabels() = runTest {
        val labels = TasksTestData.taskLabels
        dao.addAllTaskLabels(labels)
        val result = dao.getAllTasks()
        launch {
            result.take(2).firstOrNull()?.let {
                val actual = it
                println(actual)
                assertTrue { actual.size == labels.size }
            }
        }
    }

    @Test
    fun deleteLabel_WhenIdPassed_ItemWithShouldNotBeFound() = runTest {
        val labels = TasksTestData.taskLabels
        val expect = labels.last()
        dao.addAllTaskLabels(labels)
        dao.deleteTaskLabel(expect.id)
        val result = dao.getAllTasks()
        launch {
            result.test {
                val actual = awaitItem()
                assertTrue { actual.size != labels.size }
                assertFalse { actual.any { it.id == expect.id } }
            }
        }
    }

    @Test
    fun getTaskLabel_WhenCorrectIdPassed_ShouldReturnTheLabel() = runTest {
        val labels = TasksTestData.taskLabels
        val expect = labels.first()
        dao.addAllTaskLabels(labels)
        val result = dao.getTaskLabel(expect.id)
        launch {
            result.test {
                val actual = awaitItem()
                assertNotNull(actual)
                assertTrue { actual == expect }
            }
        }
    }

    @Test
    fun getTaskLabel_WhenWrongIdPassed_ShouldReturnNull() = runTest {
        val labels = TasksTestData.taskLabels
        val wrongId = "very_wrong_id"
        dao.addAllTaskLabels(labels)
        val result = dao.getTaskLabel(wrongId)
        launch {
            result.test {
                val actual = awaitItem()
                assertNull(actual) // TODO
            }
        }
    }

    @Test
    fun deleteAllLabels_ShouldLeaveAnEmptyTable() = runTest {
        val labels = TasksTestData.taskLabels
        dao.addAllTaskLabels(labels)
        dao.deleteAllTaskLabels()
        val result = dao.getAllTasks()
        launch {
            result.test {
                val actual = awaitItem()
                assertTrue { actual.isEmpty() }
            }
        }
    }
}
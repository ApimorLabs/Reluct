package work.racka.reluct.common.database.dao.tasks

import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import work.racka.reluct.common.database.di.TestPlatform
import work.racka.reluct.common.database.util.TestData
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class TasksDaoImplTest : KoinTest {

    private val dao: TasksDao by inject()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                TestPlatform.platformDatabaseModule(),
                module {
                    single<TasksDao> {
                        TasksDaoImpl(
                            coroutineScope = CoroutineScope(StandardTestDispatcher()),
                            databaseWrapper = get()
                        )
                    }
                }
            )
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun readAndWriteToDb_ProvidedATaskDbObject_ShouldReadAllTasksObjectsInDb() =
        runTest {
            val tasks = TestData.taskDbObjects
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
        val tasks = TestData.taskDbObjects
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
        val tasks = TestData.taskDbObjects
        val wrongId = "20000L"
        tasks.forEach {
            dao.insertTask(it)
        }
        val result = dao.getTask(wrongId)
        launch {
            result.test {
                val actual = awaitItem()
                assertNull(actual)
                awaitComplete()
            }
        }
    }

    @Test
    fun getPendingTasks_ReturnsAllPendingTasks() = runTest {
        val tasks = TestData.taskDbObjects
        val expectedTasks = tasks.filter { !it.done }
        tasks.forEach {
            dao.insertTask(it)
        }
        val result = dao.getPendingTasks()
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
        val tasks = TestData.taskDbObjects
        val expectedTasks = tasks.filter { it.done }
        tasks.forEach {
            dao.insertTask(it)
        }
        val result = dao.getCompletedTasks()
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
        val tasks = TestData.taskDbObjects
        val titleQuery = "Task 1"
        val descQuery = "Desc4"
        val expectedTasks1 = tasks.filter { it.title.contains(titleQuery) }
        val expectedTasks2 = tasks.filter { it.description?.contains(descQuery) ?: false }
        tasks.forEach {
            dao.insertTask(it)
        }
        val result1 = dao.searchTasks(titleQuery)
        val result2 = dao.searchTasks(descQuery)
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
        val tasks = TestData.taskDbObjects
        val query = "Task no where to be found"
        tasks.forEach {
            dao.insertTask(it)
        }
        val result = dao.searchTasks(query)
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
        val tasks = TestData.taskDbObjects
        val tasksNotDone = tasks.filter { !it.done }
        val taskNotDone = tasksNotDone.first()
        val taskDone = taskNotDone.copy(done = true)
        tasks.forEach {
            dao.insertTask(it)
        }
        dao.toggleTaskDone(id = taskNotDone.id, isDone = true, wasOverDue = false)
        val pendingTasks = dao.getPendingTasks()
        val completedTasks = dao.getCompletedTasks()
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
        val tasks = TestData.taskDbObjects
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
        val tasks = TestData.taskDbObjects
        val deletedTasks = tasks.filter { it.done }
        tasks.forEach {
            dao.insertTask(it)
        }
        dao.deleteAllCompletedTasks()
        val result = dao.getCompletedTasks()
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
        val tasks = TestData.taskDbObjects
        tasks.forEach {
            dao.insertTask(it)
        }
        dao.deleteAll()
        val result = dao.getAllTasks()
        launch {
            result.test {
                val actual = awaitItem()
                awaitComplete()
                assertTrue(actual.isEmpty())
            }
        }
    }
}
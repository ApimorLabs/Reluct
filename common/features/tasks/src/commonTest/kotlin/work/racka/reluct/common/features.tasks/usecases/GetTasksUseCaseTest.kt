package work.racka.reluct.common.features.tasks.usecases

import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.usecases.impl.GetTasksUseCaseImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.util.DataMappers.asDatabaseModel
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.features.tasks.util.TestData
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class GetTasksUseCaseTest : KoinTest {

    private val useCase: GetTasksUseCase by inject()

    @RelaxedMockK
    private lateinit var dao: TasksDao

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                module {
                    factory<GetTasksUseCase> {
                        GetTasksUseCaseImpl(
                            dao = dao,
                            backgroundDispatcher = StandardTestDispatcher()
                        )
                    }
                }
            )
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
        unmockkAll()
    }

    @Test
    fun getPendingTasks_WhenCalled_ReturnsFlowOfListOfTasksFromDb() = runTest {
        val tasksFromDb = TestData.taskDbObjects
        val expectedTasks = tasksFromDb.map { it.asTask() }

        every { dao.getPendingTasks() } returns flowOf(tasksFromDb)

        val result = useCase.getPendingTasks()
        launch {
            result.test {
                val actual = awaitItem()
                awaitComplete()
                println(actual)

                assertEquals(expectedTasks, actual)
                verify { dao.getPendingTasks() }
            }
        }
    }

    @Test
    fun getCompletedTasks_WhenCalled_ReturnsFlowOfListOfTasksFromDb() = runTest {
        val tasksFromDb = TestData.taskDbObjects
        val expectedTasks = tasksFromDb.map { it.asTask() }

        every { dao.getCompletedTasks() } returns flowOf(tasksFromDb)

        val result = useCase.getCompletedTasks()
        launch {
            result.test {
                val actual = awaitItem()
                awaitComplete()
                println(actual)

                assertEquals(expectedTasks, actual)
                verify { dao.getCompletedTasks() }
            }
        }
    }

    @Test
    fun getSearchedTasks_WhenCalledWithQuery_ReturnsFlowOfListOfTasksFromDb() = runTest {
        val tasksFromDb = TestData.taskDbObjects
        val expectedTasks = tasksFromDb.map { it.asTask() }
        val query = "some task"

        every { dao.searchTasks(query) } returns flowOf(tasksFromDb)

        val result = useCase.getSearchedTasks(query)
        launch {
            result.test {
                val actual = awaitItem()
                awaitComplete()
                println(actual)

                assertEquals(expectedTasks, actual)
                verify { dao.searchTasks(query) }
            }
        }
    }

    @Test
    fun getTask_WhenCalledWithCorrectIdAndTaskFoundInDb_ReturnsNonNullFlowOfTask() =
        runTest {
            val taskFromDb = TestData.taskDbObjects.first()
            val expectedTask = taskFromDb.asTask()
            val taskId = "1L"
            every { dao.getTask(taskId) } returns flowOf(taskFromDb)

            launch {
                val result = useCase.getTask(taskId)
                result.test {
                    val actual = awaitItem()
                    awaitComplete()
                    println(actual)

                    assertEquals(expectedTask, actual)
                    verify { dao.getTask(taskId) }
                }
            }
        }

    @Test
    fun getTask_WhenCalledWithWrongIdAndTaskFoundInDb_ReturnsNullFlowOfTask() =
        runTest {
            val taskId = "2000L"
            every { dao.getTask(taskId) } returns flowOf(null)

            launch {
                val result = useCase.getTask(taskId)
                result.test {
                    val actual = awaitItem()
                    awaitComplete()
                    println(actual)

                    assertNull(actual)
                    verify { dao.getTask(taskId) }
                }
            }
        }

    @Test
    fun getTaskToEdit_WhenCalledWithCorrectIdAndTaskFoundInDb_ReturnsNonNullFlowOfEditTask() =
        runTest {
            val expectedTask = TestData.editTask
            val taskId = "2L"
            every { dao.getTask(taskId) } returns flowOf(expectedTask.asDatabaseModel())

            launch {
                val result = useCase.getTaskToEdit(taskId)
                result.test {
                    val actual = awaitItem()
                    awaitComplete()
                    println(actual)

                    assertEquals(expectedTask, actual)
                    verify { dao.getTask(taskId) }
                }
            }
        }

    @Test
    fun getTaskToEdit_WhenCalledWithWrongIdAndTaskNotFoundInDb_ReturnsNullFlowOfEditTask() =
        runTest {
            val taskId = "200000L"
            every { dao.getTask(taskId) } returns flowOf(null)

            launch {
                val result = useCase.getTaskToEdit(taskId)
                result.test {
                    val actual = awaitItem()
                    awaitComplete()
                    println(actual)

                    assertNull(actual)
                    verify { dao.getTask(taskId) }
                }
            }
        }
}
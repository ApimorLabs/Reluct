package work.racka.reluct.common.domain.usecases

/*
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
import work.racka.reluct.common.domain.mappers.tasks.asDatabaseModel
import work.racka.reluct.common.domain.mappers.tasks.asTask
import work.racka.reluct.common.domain.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.domain.usecases.tasks.impl.ModifyTaskUseCaseImpl
import work.racka.reluct.common.domain.util.TestData
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ModifyTaskUseCaseTest : KoinTest {

    private val useCase: ModifyTaskUseCase by inject()

    @RelaxedMockK
    private lateinit var dao: TasksDao

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                module {
                    factory<ModifyTaskUseCase> {
                        ModifyTaskUseCaseImpl(
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

    @Test
    fun saveTask_WhenCalled_DaoShouldReceiveTaskToSave() = runTest {
        val task = TestData.editTask
        every { dao.insertTask(task.asDatabaseModel()) } returns Unit

        useCase.saveTask(task)

        verify { dao.insertTask(task.asDatabaseModel()) }
    }

    @Test
    fun deleteTask_WhenCalled_ShouldCallDaoMethodWithCorrectTaskId() = runTest {
        val taskId = "2L"
        every { dao.deleteTask(taskId) } returns Unit

        useCase.deleteTask(taskId)

        verify { dao.deleteTask(taskId) }
    }

    @Test
    fun toggleTaskDone_WhenCalled_ShouldCallDaoMethodWithCorrectValues() = runTest {
        val taskId = "2L"
        val isDone = true
        val dbObject = TestData.taskDbObjects.first()
        val task = dbObject.asTask().copy(id = taskId, done = isDone)
        // We used any because completedLocalDateTime is computed from current time
        every { dao.toggleTaskDone(task.id, isDone, task.overdue, any()) } returns Unit

        useCase.toggleTaskDone(task, isDone)

        verify { dao.toggleTaskDone(task.id, isDone, task.overdue, any()) }
    }
}*/

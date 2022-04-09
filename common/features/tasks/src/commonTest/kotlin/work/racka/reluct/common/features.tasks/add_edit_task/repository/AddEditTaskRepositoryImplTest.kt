package work.racka.reluct.common.features.tasks.add_edit_task.repository

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
import work.racka.reluct.common.features.tasks.util.DataMappers.asDatabaseModel
import work.racka.reluct.common.features.tasks.util.TestData
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditTaskRepositoryImplTest : KoinTest {

    private val repo: AddEditTaskRepository by inject()

    @RelaxedMockK
    private lateinit var dao: TasksDao

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                module {
                    factory<AddEditTaskRepository> {
                        AddEditTaskRepositoryImpl(
                            dao = dao,
                            backgroundDispatcher = Dispatchers.Default
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
    fun addTask_WhenCalled_DaoShouldReceiveTaskToSave() = runTest {
        val task = TestData.editTask
        every { dao.insertTask(task.asDatabaseModel()) } returns Unit

        repo.addTask(task)

        verify { dao.insertTask(task.asDatabaseModel()) }
    }

    @Test
    fun getTaskToEdit_WhenCalledWithIdAndTaskFoundInDb_ReturnsNonNullFlowOfEditTask() = runTest {
        val expectedTask = TestData.editTask
        val taskId = 2L
        every { dao.getTask(taskId) } returns flowOf(expectedTask.asDatabaseModel())

        launch {
            val result = repo.getTaskToEdit(taskId)
            result.test {
                val actual = expectMostRecentItem()
                println(actual)

                assertEquals(expectedTask, actual)
                verify { dao.getTask(taskId) }
            }
        }
    }

    @Test
    fun getTaskToEdit_WhenCalledWithIdAndTaskNotFoundInDb_ReturnsNullFlowOfEditTask() = runTest {
        val taskId = 2L
        every { dao.getTask(taskId) } returns flowOf(null)

        launch {
            val result = repo.getTaskToEdit(taskId)
            result.test {
                val actual = expectMostRecentItem()
                println("Result is :$actual")

                assertNull(actual)
                verify { dao.getTask(taskId) }
            }
        }
    }
}
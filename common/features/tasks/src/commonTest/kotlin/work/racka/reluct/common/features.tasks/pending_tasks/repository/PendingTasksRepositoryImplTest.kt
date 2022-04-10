package work.racka.reluct.common.features.tasks.pending_tasks.repository

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
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.features.tasks.util.TestData
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PendingTasksRepositoryImplTest : KoinTest {

    private val repo: PendingTasksRepository by inject()

    @RelaxedMockK
    private lateinit var dao: TasksDao

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                module {
                    factory<PendingTasksRepository> {
                        PendingTasksRepositoryImpl(
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
    fun getTasks_WhenCalled_ReturnsFlowOfListOfTasksFromDb() = runTest {
        val tasksFromDb = TestData.taskDbObjects
        val expectedTasks = tasksFromDb.map { it.asTask() }

        every { dao.getPendingTasks() } returns flowOf(tasksFromDb)

        val result = repo.getTasks()
        launch {
            result.test {
                val actual = awaitItem()
                println(actual)

                assertEquals(expectedTasks, actual)
                verify { dao.getPendingTasks() }
                awaitComplete()
            }
        }
    }


    @Test
    fun toggleTaskDone_WhenCalled_ShouldCallDaoMethod() = runTest {
        val taskId = 2L
        val isDone = true

        every { dao.toggleTaskDone(taskId, isDone) } returns Unit

        repo.toggleTaskDone(taskId, isDone)

        verify { dao.toggleTaskDone(taskId, isDone) }
    }
}
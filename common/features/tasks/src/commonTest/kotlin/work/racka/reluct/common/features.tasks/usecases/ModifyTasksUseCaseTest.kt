package work.racka.reluct.common.features.tasks.usecases

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.usecases.impl.ModifyTasksUseCaseImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase
import work.racka.reluct.common.features.tasks.util.DataMappers.asDatabaseModel
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.features.tasks.util.TestData
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ModifyTasksUseCaseTest : KoinTest {

    private val useCase: ModifyTasksUseCase by inject()

    @RelaxedMockK
    private lateinit var dao: TasksDao

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                module {
                    factory<ModifyTasksUseCase> {
                        ModifyTasksUseCaseImpl(
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
    fun addTask_WhenCalled_DaoShouldReceiveTaskToSave() = runTest {
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
        val task = TestData.taskDbObjects.first()
            .asTask()
            .copy(id = taskId, done = isDone)
        every { dao.toggleTaskDone(task.id, isDone, task.overdue) } returns Unit

        useCase.toggleTaskDone(task, isDone)

        verify { dao.toggleTaskDone(task.id, isDone, task.overdue) }
    }
}
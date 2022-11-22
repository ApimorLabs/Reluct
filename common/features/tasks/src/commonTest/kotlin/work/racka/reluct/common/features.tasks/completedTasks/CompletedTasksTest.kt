package work.racka.reluct.common.features.tasks.completedTasks

import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineScope
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
import work.racka.reluct.common.domain.mappers.tasks.asTask
import work.racka.reluct.common.domain.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.domain.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.util.TestData
import work.racka.reluct.common.model.states.tasks.CompletedTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CompletedTasksTest : KoinTest {

    private val completedTasks: CompletedTasks by inject()

    @RelaxedMockK
    private lateinit var getTasksUseCase: GetTasksUseCase

    @RelaxedMockK
    private lateinit var modifyTasksUsesCase: ModifyTaskUseCase

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                module {
                    factory<CompletedTasks> {
                        CompletedTasksImpl(
                            getTasksUseCase = getTasksUseCase,
                            modifyTasksUsesCase = modifyTasksUsesCase,
                            scope = CoroutineScope(StandardTestDispatcher())
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
    fun getCompletedTasks_OnClassInit_ShouldUpdateUIStateWithCompletedTasks() =
        runTest {
            val factor = 1L
            val tasks = TestData.taskDbObjects.map { it.asTask() }
            val expectedState = CompletedTasksState.Data(
                tasks = tasks.groupBy { it.dueDate }
            )

            coEvery { getTasksUseCase.getCompletedTasks(factor) } returns flowOf(tasks)

            val result = completedTasks.uiState
            launch {
                result.test {
                    val initial = awaitItem()
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(initial is CompletedTasksState.Loading)
                    assertTrue(actual is CompletedTasksState.Data)
                    assertEquals(expectedState, actual)
                    coVerify { getTasksUseCase.getCompletedTasks(factor) }
                    awaitComplete()
                }
            }
        }

    @Test
    fun toggleDone_WhenCalled_ShouldCallRepoMethodAndProduceATaskDoneSideEffect() =
        runTest {
            val taskId = "2L"
            val isDone = true
            val task = TestData.taskDbObjects.first()
                .asTask()
                .copy(id = taskId, done = isDone)
            val expectedEvent = TasksEvents.ShowMessageDone(isDone, task.title)
            coEvery { modifyTasksUsesCase.toggleTaskDone(task, isDone) } returns Unit

            val result = completedTasks.events
            launch {
                result.test {
                    completedTasks.toggleDone(task, isDone)
                    val actual = expectMostRecentItem()
                    println(actual)

                    assertEquals(expectedEvent, actual)
                    coVerify { modifyTasksUsesCase.toggleTaskDone(task, isDone) }
                }
            }
        }

    @Test
    fun navigateToTaskDetails_WhenProvidedTaskId_ProvidesNavigateToDetailsEventWithGivenId() =
        runTest {
            val taskId = "2L"
            val expectedEvent = TasksEvents.Navigation.NavigateToTaskDetails(taskId)


            val result = completedTasks.events
            launch {
                result.test {
                    completedTasks.navigateToTaskDetails(taskId)
                    val actual = expectMostRecentItem()
                    println(actual)

                    assertEquals(expectedEvent, actual)
                }
            }
        }
}
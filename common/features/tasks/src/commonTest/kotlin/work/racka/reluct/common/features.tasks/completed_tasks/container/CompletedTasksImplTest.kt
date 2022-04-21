package work.racka.reluct.common.features.tasks.completed_tasks.container

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
import work.racka.reluct.common.features.tasks.completed_tasks.repository.CompletedTasksRepository
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.features.tasks.util.TestData
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CompletedTasksImplTest : KoinTest {

    private val completedTasks: CompletedTasks by inject()

    @RelaxedMockK
    private lateinit var repo: CompletedTasksRepository

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                module {
                    factory<CompletedTasks> {
                        CompletedTasksImpl(
                            completedTasks = repo,
                            scope = CoroutineScope(Dispatchers.Default)
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
            val tasks = TestData.taskDbObjects.map { it.asTask() }
            val expectedState = TasksState.CompletedTasks(
                tasks = tasks.groupBy { it.dueDate }
            )

            coEvery { repo.getTasks() } returns flowOf(tasks)

            val result = completedTasks.uiState
            launch {
                result.test {
                    val initial = awaitItem()
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(initial is TasksState.Loading)
                    assertTrue(actual is TasksState.CompletedTasks)
                    assertEquals(expectedState, actual)
                    coVerify { repo.getTasks() }
                    awaitComplete()
                }
            }
        }

    @Test
    fun toggleDone_WhenCalled_ShouldCallRepoMethodAndProduceATaskDoneSideEffect() =
        runTest {
            val taskId = "2L"
            val isDone = true
            val expectedEvent = TasksSideEffect.ShowMessageDone(isDone)
            coEvery { repo.toggleTaskDone(taskId, isDone) } returns Unit

            val result = completedTasks.events
            launch {
                result.test {
                    completedTasks.toggleDone(taskId, isDone)
                    val actual = expectMostRecentItem()
                    println(actual)

                    assertEquals(expectedEvent, actual)
                    coVerify { repo.toggleTaskDone(taskId, isDone) }
                }
            }
        }

    @Test
    fun navigateToTaskDetails_WhenProvidedTaskId_ProvidesNavigateToDetailsEventWithGivenId() =
        runTest {
            val taskId = "2L"
            val expectedEvent = TasksSideEffect.Navigation.NavigateToTaskDetails(taskId)


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
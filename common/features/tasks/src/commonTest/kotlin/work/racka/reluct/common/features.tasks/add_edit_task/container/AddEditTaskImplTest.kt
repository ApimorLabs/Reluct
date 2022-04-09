package work.racka.reluct.common.features.tasks.add_edit_task.container

import app.cash.turbine.test
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
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
import work.racka.reluct.common.features.tasks.add_edit_task.repository.AddEditTaskRepository
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.features.tasks.util.TestData
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditTaskImplTest : KoinTest {
    private val addEditTask: AddEditTask by inject()

    @RelaxedMockK
    lateinit var repo: AddEditTaskRepository

    private val taskId = 2L

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                module {
                    single<AddEditTask> {
                        AddEditTaskImpl(
                            addEditTask = repo,
                            taskId = taskId,
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
    fun getTasks_OnClassInit_WhenTaskIdIsNotNull_ProvidesAddEditTaskUIStateWithTask() =
        runTest {
            val expectedTask = TestData.editTask
            coEvery { repo.getTaskToEdit(taskId) } returns flowOf(expectedTask)

            val result = addEditTask.uiState
            launch {
                result.test {
                    val actual = expectMostRecentItem()
                    println(actual)

                    assertTrue(actual is TasksState.AddEditTask)
                    assertEquals(expectedTask, actual.task)
                    coVerify { repo.getTaskToEdit(taskId) }
                }
            }
        }

    @Test
    fun getTasks_OnClassInit_WhenTaskIdIsNull_ProvidesEmptyAddEditTaskUIState() =
        runTest {
            val expectedState = TasksState.EmptyAddEditTask
            val myRepo: AddEditTaskRepository = mockk()
            val addEditTaskWithNullTaskId: AddEditTask = AddEditTaskImpl(
                addEditTask = myRepo,
                taskId = null,
                scope = CoroutineScope(Dispatchers.Default)
            )

            val result = addEditTaskWithNullTaskId.uiState
            launch {
                result.test {
                    val actual = expectMostRecentItem()
                    println(actual)

                    assertEquals(expectedState, actual)
                    coVerify(exactly = 0) { repo.getTaskToEdit(taskId) }
                }
            }
        }


    @Test
    fun getTasks_OnClassInit_WhenTaskNotFoundInDb_ProvidesEmptyAddEditTaskUIState() =
        runTest {
            val expectedState = TasksState.EmptyAddEditTask
            coEvery { repo.getTaskToEdit(taskId) } returns flowOf(null)

            val result = addEditTask.uiState
            launch {
                result.test {
                    val actual = expectMostRecentItem()
                    println(actual)

                    assertEquals(expectedState, actual)
                    assertNull((actual as TasksState.AddEditTask).task)
                    coVerify { repo.getTaskToEdit(taskId) }
                }
            }
        }


    @Test
    fun saveTask_WhenTaskSaved_CallsSaveMethodOnRepo_Then_ProvidesShowSnackbarEvent() =
        runTest {
            val expectedEvent = TasksSideEffect.ShowSnackbar(Constants.TASK_SAVED)
            val task = TestData.editTask
            coEvery { repo.addTask(task) } returns Unit

            addEditTask.saveTask(task)

            val result = addEditTask.events
            launch {
                result.test {
                    val actual = expectMostRecentItem()
                    println(actual)

                    assertEquals(expectedEvent, actual)
                    coVerify { repo.addTask(task) }
                }
            }
        }

    @Test
    fun goBack_WhenGoBackCalled_ProducesGoBackNavigationEvent() = runTest {
        val expectedEvent = TasksSideEffect.Navigation.GoBack
        addEditTask.goBack()
        val result = addEditTask.events
        launch {
            result.test {
                val actual = expectMostRecentItem()
                println(actual)

                assertEquals(expectedEvent, actual)
            }
        }
    }
}
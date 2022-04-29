package work.racka.reluct.common.features.tasks.add_edit_task

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
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.features.tasks.util.TestData
import work.racka.reluct.common.model.states.tasks.TasksSideEffect
import work.racka.reluct.common.model.states.tasks.TasksState
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditTaskTest : KoinTest {
    private val addEditTask: AddEditTask by inject()

    @RelaxedMockK
    private lateinit var getTasksUseCase: GetTasksUseCase

    @RelaxedMockK
    private lateinit var modifyTasksUsesCase: ModifyTasksUseCase

    private val taskId = "2L"

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                module {
                    factory<AddEditTask> {
                        AddEditTaskImpl(
                            getTasksUseCase = getTasksUseCase,
                            modifyTasksUseCase = modifyTasksUsesCase,
                            taskId = taskId,
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
    fun getTasks_OnClassInit_WhenTaskIdIsNotNull_ProvidesAddEditTaskUIStateWithTask() =
        runTest {
            val expectedTask = TestData.editTask
            coEvery { getTasksUseCase.getTaskToEdit(taskId) } returns flowOf(expectedTask)

            val result = addEditTask.uiState
            launch {
                result.test {
                    val initial = awaitItem()
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(initial is TasksState.Loading)
                    assertTrue(actual is TasksState.AddEditTask)
                    assertEquals(expectedTask, actual.task)
                    coVerify { getTasksUseCase.getTaskToEdit(taskId) }
                    awaitComplete()
                }
            }
        }

    @Test
    fun getTasks_OnClassInit_WhenTaskIdIsNull_ProvidesEmptyAddEditTaskUIState() =
        runTest {
            val expectedState = TasksState.EmptyAddEditTask
            val myGetTasksUseCase: GetTasksUseCase = mockk()
            val myModifyTasksUsesCase: ModifyTasksUseCase = mockk()
            val addEditTaskWithNullTaskId: AddEditTask = AddEditTaskImpl(
                getTasksUseCase = myGetTasksUseCase,
                modifyTasksUseCase = myModifyTasksUsesCase,
                taskId = null,
                scope = CoroutineScope(StandardTestDispatcher())
            )

            val result = addEditTaskWithNullTaskId.uiState
            launch {
                result.test {
                    val initial = awaitItem()
                    val resetState = awaitItem()
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(initial is TasksState.Loading)
                    assertEquals(TasksState.EmptyAddEditTask, resetState)
                    assertEquals(expectedState, actual)
                    coVerify(exactly = 0) { myGetTasksUseCase.getTaskToEdit(taskId) }
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }


    @Test
    fun getTasks_OnClassInit_WhenTaskNotFoundInDb_ProvidesEmptyAddEditTaskUIState() =
        runTest {
            val expectedState = TasksState.EmptyAddEditTask
            coEvery { getTasksUseCase.getTaskToEdit(taskId) } returns flowOf(null)

            val result = addEditTask.uiState
            launch {
                result.test {
                    val initial = awaitItem()
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(initial is TasksState.Loading)
                    assertEquals(expectedState, actual)
                    assertNull((actual as TasksState.AddEditTask).task)
                    coVerify { getTasksUseCase.getTaskToEdit(taskId) }
                    awaitComplete()
                }
            }
        }


    @Test
    fun saveTask_WhenTaskSaved_CallsSaveMethodOnRepo_Then_ProvidesShowSnackbarEvent() =
        runTest {
            val expectedEvent = TasksSideEffect.ShowMessage(Constants.TASK_SAVED)
            val expectedUiState = TasksState.AddEditTask(taskSaved = true)
            val task = TestData.editTask
            coEvery { modifyTasksUsesCase.addTask(task) } returns Unit

            addEditTask.saveTask(task)

            val events = addEditTask.events
            val uiState = addEditTask.uiState
            launch {
                events.test {
                    val actual = expectMostRecentItem()
                    println(actual)

                    assertEquals(expectedEvent, actual)
                }
                uiState.test {
                    val initial = awaitItem()
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(initial is TasksState.Loading)
                    assertEquals(expectedUiState, actual)
                }
                coVerify { modifyTasksUsesCase.addTask(task) }
            }
        }

    @Test
    fun goBack_WhenGoBackCalled_ProducesGoBackNavigationEvent() = runTest {
        val expectedEvent = TasksSideEffect.Navigation.GoBack
        val result = addEditTask.events
        launch {
            result.test {
                addEditTask.goBack()
                val actual = expectMostRecentItem()
                println(actual)

                assertEquals(expectedEvent, actual)
            }
        }
    }
}
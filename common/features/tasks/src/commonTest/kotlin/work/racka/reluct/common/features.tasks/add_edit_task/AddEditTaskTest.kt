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
import work.racka.reluct.common.model.states.tasks.AddEditTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents
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

                    assertTrue(initial is AddEditTasksState.Loading)
                    assertTrue(actual is AddEditTasksState.Data)
                    assertEquals(expectedTask, actual.task)
                    coVerify { getTasksUseCase.getTaskToEdit(taskId) }
                    awaitComplete()
                }
            }
        }

    @Test
    fun getTasks_OnClassInit_WhenTaskIdIsNull_ProvidesAddEditTaskUIStateWithNullTask() =
        runTest {
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
                    val actual = expectMostRecentItem()
                    println(actual)

                    assertTrue(actual is AddEditTasksState.Data)
                    assertNull(actual.task)
                    coVerify(exactly = 0) { myGetTasksUseCase.getTaskToEdit(taskId) }
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }


    @Test
    fun getTasks_OnClassInit_WhenTaskNotFoundInDb_ProvidesAddEditTaskUIStateWithNullTask() =
        runTest {
            coEvery { getTasksUseCase.getTaskToEdit(taskId) } returns flowOf(null)

            val result = addEditTask.uiState
            launch {
                result.test {
                    val initial = awaitItem()
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(initial is AddEditTasksState.Loading)
                    assertTrue(actual is AddEditTasksState.Data)
                    assertNull(actual.task)
                    coVerify { getTasksUseCase.getTaskToEdit(taskId) }
                    awaitComplete()
                }
            }
        }


    @Test
    fun saveTask_WhenNewTaskSaved_AndTaskIdIsNull_CallsSaveMethodOnRepo_Then_ProvidesShowMessageEventAndGoBackNavigationEvent() =
        runTest {
            val myGetTasksUseCase: GetTasksUseCase = mockk()
            val myModifyTasksUsesCase: ModifyTasksUseCase = mockk()
            val addEditTaskWithNullTaskId: AddEditTask = AddEditTaskImpl(
                getTasksUseCase = myGetTasksUseCase,
                modifyTasksUseCase = myModifyTasksUsesCase,
                taskId = null,
                scope = CoroutineScope(StandardTestDispatcher())
            )

            val expectedEvent = TasksEvents.ShowMessage(Constants.TASK_SAVED)
            val task = TestData.editTask
            coEvery { myModifyTasksUsesCase.addTask(task) } returns Unit


            val events = addEditTaskWithNullTaskId.events
            val uiState = addEditTaskWithNullTaskId.uiState
            launch {
                addEditTaskWithNullTaskId.saveTask(task)
                events.test {
                    val actual = awaitItem()
                    println(actual)

                    assertEquals(expectedEvent, actual)
                    awaitComplete()
                }
                uiState.test {
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(actual is AddEditTasksState.TaskSaved)
                    awaitComplete()
                }
                coVerify { myModifyTasksUsesCase.addTask(task) }
            }
        }

    @Test
    fun saveTask_WhenEditedTaskSaved_AndTaskIdIsNotNull_CallsSaveMethodOnRepo_Then_ProvidesShowMessageEventAndTaskSavedUIState() =
        runTest {
            val expectedEvent = TasksEvents.ShowMessage(Constants.TASK_SAVED)
            val task = TestData.editTask
            coEvery { modifyTasksUsesCase.addTask(task) } returns Unit


            val events = addEditTask.events
            val uiState = addEditTask.uiState
            launch {
                addEditTask.saveTask(task)
                events.test {
                    val event1 = awaitItem()
                    val event2 = awaitItem()
                    println(event1)
                    println(event2)

                    assertEquals(expectedEvent, event1)
                    assertTrue(event2 is TasksEvents.Navigation.GoBack)
                    awaitComplete()
                }
                uiState.test {
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(actual is AddEditTasksState.TaskSaved)
                    awaitComplete()
                }
                coVerify { modifyTasksUsesCase.addTask(task) }
            }
        }

    @Test
    fun goBack_WhenGoBackCalled_ProducesGoBackNavigationEvent() = runTest {
        val expectedEvent = TasksEvents.Navigation.GoBack
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
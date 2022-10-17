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
import work.racka.reluct.common.domain.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.tasks.util.Constants
import work.racka.reluct.common.features.tasks.util.TestData
import work.racka.reluct.common.model.states.tasks.AddEditTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditTaskTest : KoinTest {
    private val addEditTask: AddEditTask by inject()

    @RelaxedMockK
    private lateinit var modifyTasksUsesCase: ModifyTaskUseCase

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
                            modifyTaskUseCase = modifyTasksUsesCase,
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
            coEvery { modifyTasksUsesCase.getTaskToEdit(taskId) } returns flowOf(expectedTask)

            val result = addEditTask.uiState
            launch {
                result.test {
                    val initial = awaitItem()
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(initial is AddEditTasksState.Loading)
                    assertTrue(actual is AddEditTasksState.Data)
                    assertEquals(expectedTask, actual.task)
                    coVerify { modifyTasksUsesCase.getTaskToEdit(taskId) }
                    awaitComplete()
                }
            }
        }

    @Test
    fun getTasks_OnClassInit_WhenTaskIdIsNull_ProvidesAddEditTaskUIStateWithNullTask() =
        runTest {
            val myModifyTasksUsesCase: ModifyTaskUseCase = mockk()
            val addEditTaskWithNullTaskId: AddEditTask = AddEditTaskImpl(
                modifyTaskUseCase = myModifyTasksUsesCase,
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
                    coVerify(exactly = 0) { myModifyTasksUsesCase.getTaskToEdit(taskId) }
                }
            }
        }


    @Test
    fun getTasks_OnClassInit_WhenTaskNotFoundInDb_ProvidesAddEditTaskUIStateWithNullTask() =
        runTest {
            coEvery { modifyTasksUsesCase.getTaskToEdit(taskId) } returns flowOf(null)

            val result = addEditTask.uiState
            launch {
                result.test {
                    val initial = awaitItem()
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(initial is AddEditTasksState.Loading)
                    assertTrue(actual is AddEditTasksState.Data)
                    assertNull(actual.task)
                    coVerify { modifyTasksUsesCase.getTaskToEdit(taskId) }
                    awaitComplete()
                }
            }
        }


    @Test
    fun saveTask_WhenNewTaskSaved_AndTaskIdIsNull_CallsSaveMethodOnRepo_Then_ProvidesShowMessageEventAndGoBackNavigationEvent() =
        runTest {
            val myModifyTasksUsesCase: ModifyTaskUseCase = mockk()
            val addEditTaskWithNullTaskId: AddEditTask = AddEditTaskImpl(
                modifyTaskUseCase = myModifyTasksUsesCase,
                taskId = null,
                scope = CoroutineScope(StandardTestDispatcher())
            )

            val expectedEvent = TasksEvents.ShowMessage(Constants.TASK_SAVED)
            val task = TestData.editTask
            coEvery { myModifyTasksUsesCase.saveTask(task) } returns Unit


            val events = addEditTaskWithNullTaskId.events
            val uiState = addEditTaskWithNullTaskId.uiState
            launch {
                addEditTaskWithNullTaskId.saveTask(task)
                events.test {
                    val event1 = awaitItem()
                    val event2 = awaitItem()
                    println(event1)
                    println(event2)

                    assertTrue(event1 is TasksEvents.Nothing)
                    assertEquals(expectedEvent, event2)
                    awaitComplete()
                }
                uiState.test {
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(actual is AddEditTasksState.TaskSaved)
                    awaitComplete()
                }
                coVerify { myModifyTasksUsesCase.saveTask(task) }
            }
        }

    @Test
    fun saveTask_WhenEditedTaskSaved_AndTaskIdIsNotNull_CallsSaveMethodOnRepo_Then_ProvidesShowMessageEventAndTaskSavedUIState() =
        runTest {
            val expectedEvent = TasksEvents.ShowMessage(Constants.TASK_SAVED)
            val task = TestData.editTask
            coEvery { modifyTasksUsesCase.saveTask(task) } returns Unit


            val events = addEditTask.events
            val uiState = addEditTask.uiState
            launch {
                addEditTask.saveTask(task)
                events.test {
                    val event1 = awaitItem()
                    val event2 = awaitItem()
                    val event3 = awaitItem()
                    println(event1)
                    println(event2)
                    println(event3)

                    assertTrue(event1 is TasksEvents.Nothing)
                    assertEquals(expectedEvent, event2)
                    assertTrue(event3 is TasksEvents.Navigation.GoBack)
                    awaitComplete()
                }
                uiState.test {
                    val actual = awaitItem()
                    println(actual)

                    assertTrue(actual is AddEditTasksState.TaskSaved)
                    awaitComplete()
                }
                coVerify { modifyTasksUsesCase.saveTask(task) }
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
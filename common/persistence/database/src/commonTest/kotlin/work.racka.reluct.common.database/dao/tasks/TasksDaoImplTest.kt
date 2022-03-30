package work.racka.reluct.common.database.dao.tasks

import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import work.racka.reluct.common.database.di.TestPlatform
import work.racka.reluct.common.database.util.TestData
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TasksDaoImplTest : KoinTest {

    private val dao: TasksDao by inject()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                TestPlatform.platformDatabaseModule(),
                module {
                    single<TasksDao> {
                        TasksDaoImpl(
                            coroutineScope = CoroutineScope(StandardTestDispatcher()),
                            databaseWrapper = get()
                        )
                    }
                }
            )
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun readAndWriteToDb_ProvidedATaskDbObject_ShouldReadAllTasksObjectsInDb() =
        runTest {
            val tasks = TestData.taskDbObjects
            tasks.forEach {
                dao.insertTask(it)
            }
            val dbObjects = dao.getAllTasks()
            launch {
                dbObjects.test {
                    val result = awaitItem()

                    assertTrue(result.isNotEmpty())
                    assertTrue(result.containsAll(tasks))
                }
            }
        }
}
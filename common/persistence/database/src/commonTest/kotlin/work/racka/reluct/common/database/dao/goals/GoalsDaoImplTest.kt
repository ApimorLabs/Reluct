package work.racka.reluct.common.database.dao.goals

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import work.racka.reluct.common.database.di.TestPlatform
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class GoalsDaoImplTest : KoinTest {

    private val dao: GoalsDao by inject()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        startKoin {
            modules(
                TestPlatform.platformDatabaseModule(),
                module {
                    single<GoalsDao> {
                        GoalsDaoImpl(
                            dispatcher = StandardTestDispatcher(),
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
}
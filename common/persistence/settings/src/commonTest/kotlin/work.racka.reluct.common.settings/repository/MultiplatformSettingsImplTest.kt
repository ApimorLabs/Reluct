package work.racka.reluct.common.settings.repository

import app.cash.turbine.test
import com.russhwolf.settings.MockSettings
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
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MultiplatformSettingsImplTest : KoinTest {
    private val repo: MultiplatformSettings by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            Dispatchers.setMain(StandardTestDispatcher())
            modules(
                module {
                    single<MultiplatformSettings> {
                        val settings = MockSettings()
                        MultiplatformSettingsImpl(settings)
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
    fun testDefaultValueForReadThemeSettings_ShouldReturn_Negative_1() = runTest {
        val expect = -1
        val data = repo.theme
        launch {
            data.test {
                val actual = expectMostRecentItem()
                assertEquals(expect, actual)
            }
        }
    }

    @Test
    fun saveThemeSettingsAndReadThemeSettings_ShouldReturnSameValueOnRead() = runTest {
        val expect = 2
        repo.saveThemeSettings(expect)
        val data = repo.theme
        launch {
            data.test {
                val actual = expectMostRecentItem()
                assertEquals(expect, actual)
            }
        }
    }
}
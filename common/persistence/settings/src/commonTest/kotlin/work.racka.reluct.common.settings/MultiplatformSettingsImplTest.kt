package work.racka.reluct.common.settings

import app.cash.turbine.test
import com.russhwolf.settings.MockSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MultiplatformSettingsImplTest {
    private lateinit var repo: MultiplatformSettings

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        val settings = MockSettings()
        repo = MultiplatformSettingsImpl(settings)
    }

    @AfterTest
    fun teardown() {
    }

    @Test
    fun testDefaultValueForReadThemeSettings_ShouldReturn_Negative_1() = runTest {
        val expect = -1
        val data = repo.theme
        launch {
            data.test {
                val actual = expectMostRecentItem()
                println(actual)
                assertEquals(expect, actual)
            }
        }
    }

    @Test
    fun saveThemeSettingsAndReadThemeSettings_ShouldReturnSameValueOnRead() = runTest {
        println("Testing Theme Settings")
        val expect = 2
        repo.saveThemeSettings(expect)
        val data = repo.theme
        launch {
            data.test {
                val actual = expectMostRecentItem()
                println(actual)
                assertEquals(expect, actual)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun saveAndReadFocusMode_ShouldReturnSameValueOnRead() = runTest {
        println("Testing Focus Mode")
        val expect = true
        repo.saveFocusMode(expect)
        val data = repo.focusMode
        launch {
            data.test {
                val actual = expectMostRecentItem()
                println(actual)
                assertEquals(expect, actual)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun saveAndReadDoNotDisturb_ShouldReturnSameValueOnRead() = runTest {
        println("Testing Do Not Disturb")
        val expect = true
        repo.saveDoNotDisturb(expect)
        val data = repo.doNoDisturb
        launch {
            data.test {
                val actual = expectMostRecentItem()
                println(actual)
                assertEquals(expect, actual)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun saveAndReadSavedVersionCode_ShouldReturnSameValueOnRead() = runTest {
        println("Testing Saved Version Code")
        val expect = 20
        repo.saveVersionCode(expect)
        val data = repo.savedVersionCode
        launch {
            data.test {
                val actual = expectMostRecentItem()
                println(actual)
                assertEquals(expect, actual)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun saveAndReadOnBoardingShown_ShouldReturnSameValueOnRead() = runTest {
        println("Testing On Boarding Shown")
        val expect = true
        repo.saveOnBoardingShown(expect)
        val data = repo.onBoardingShown
        launch {
            data.test {
                val actual = expectMostRecentItem()
                println(actual)
                assertEquals(expect, actual)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun saveAndReadAppBlocking_ShouldReturnSameValueOnRead() = runTest {
        println("Testing App Blocking")
        val expect = false
        repo.saveAppBlocking(expect)
        val data = repo.appBlockingEnabled
        launch {
            data.test {
                val actual = expectMostRecentItem()
                println(actual)
                assertEquals(expect, actual)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}
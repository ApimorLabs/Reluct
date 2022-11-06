package work.racka.reluct.android.benchmark.baselineProfile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import work.racka.reluct.android.benchmark.util.delayUi

class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun appStartUp() {
        baselineProfileRule.collectBaselineProfile(packageName = "work.racka.reluct") {
            pressHome()
            startActivityAndWait()
        }
    }

    @Test
    fun generateProfile() {
        baselineProfileRule.collectBaselineProfile(packageName = "work.racka.reluct") {
            pressHome()
            startActivityAndWait()
            with(device) {
                // Scroll Dashboard Overview Screen
                delayUi(1000)
                findObject(By.scrollable(true)).run {
                    fling(Direction.DOWN)
                    fling(Direction.UP)
                }
                // Open Statistics and Scroll
                findObject(By.text("Statistics")).click()
                delayUi(2000)
                findObject(By.scrollable(true)).run {
                    fling(Direction.DOWN)
                    fling(Direction.UP)
                }

                // Open the Tasks Screen and scroll
                findObject(By.text("Tasks")).click()
                delayUi(2000)

                // Open new Task screen and cancel
                findObject(By.text("New Task")).click()
                pressBack()
                findObject(By.text("OK")).click()

                // Scroll Tasks Screen
                findObject(By.scrollable(true)).run {
                    fling(Direction.DOWN)
                    fling(Direction.UP)
                }

                // Open Screen Time screen and Scroll
                findObject(By.text("Screen Time")).click()
                delayUi(5000)
                findObject(By.scrollable(true)).run {
                    fling(Direction.DOWN)
                    fling(Direction.UP)
                }

                // Open Limits Page and wait to load
                findObject(By.text("Limits")).click()
                delayUi(2000)

                // Open Goals Page and Scroll
                findObject(By.text("Goals")).click()
                delayUi(2000)
                findObject(By.scrollable(true)).run {
                    fling(Direction.DOWN)
                    fling(Direction.UP)
                }

                // Go to start screen
                pressBack()
            }
        }
    }
}
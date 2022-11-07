package work.racka.reluct.android.benchmark.baselineProfile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import work.racka.reluct.android.benchmark.util.delayUi

class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun appStartUpOnly() {
        baselineProfileRule.collectBaselineProfile(packageName = "work.racka.reluct") {
            pressHome()
            startActivityAndWait()
        }
    }

    @Test
    fun appStartUpWithInitialSetup() {
        baselineProfileRule.collectBaselineProfile(packageName = "work.racka.reluct") {
            pressHome()
            startActivityAndWait()
            device.setupFlow()
        }
    }

    @Test
    fun generateProfile() {
        baselineProfileRule.collectBaselineProfile(packageName = "work.racka.reluct") {
            pressHome()
            startActivityAndWait()
            with(device) {
                // Configure app on first launch
                setupFlow()

                // Open Statistics and Scroll
                wait(Until.hasObject(By.text("Statistics")), 10_000)
                findObject(By.text("Statistics")).click()

                // Open the Tasks Screen and scroll
                wait(Until.hasObject(By.text("Tasks")), 10_000)
                findObject(By.text("Tasks")).click()

                // Open new Task screen and cancel
                wait(Until.hasObject(By.text("New Task")), 10_000)
                findObject(By.text("New Task")).click()
                Thread.sleep(2000)
                pressBack()
                findObject(By.text("OK")).click()

                // Open Task Statistics Screen
                wait(Until.hasObject(By.text("Statistics")), 10_000)
                findObject(By.text("Statistics")).click()

                // Open Screen Time screen and Scroll
                wait(Until.hasObject(By.text("Screen Time")), 10_000)
                findObject(By.text("Screen Time")).click()
                Thread.sleep(3000)
                wait(Until.hasObject(By.scrollable(true)), 10_000)
                findObject(By.scrollable(true)).run {
                    fling(Direction.DOWN)
                    fling(Direction.UP)
                }

                // Open Limits Page and wait to load
                wait(Until.hasObject(By.text("Limits")), 10_000)
                findObject(By.text("Limits")).click()

                // Ignore Goals Pages to reduce size
                /*// Open Goals Pages
                wait(Until.hasObject(By.text("Goals")), 10_000)
                findObject(By.text("Goals")).click()
                Thread.sleep(2000)
                wait(Until.hasObject(By.text("Inactive")), 10_000)
                findObject(By.text("Inactive")).click()
                Thread.sleep(2000)

                // Go to start screen
                pressBack()
                pressBack()*/
                pressBack()
            }
        }
    }
}

/**
 * Make sure the device system navigation is set to 3 buttons
 * This is hardcoded to run on Pixel devices on Android 13 and English language.
 * Adjust text names accordingly for other devices
 *
 * NOTE: I can't get it to identify "Click to Grant Permission" buttons for some reason
 * As a temporary workaround you have to Grant permissions for Notifications and Usage Access yourself
 * I have a added a delay of 5 seconds for the Notification permission and 30 seconds for Usage Access
 */
private fun UiDevice.setupFlow() {
    findObject(By.text("Next"))?.run {
        click()
        waitForIdle()

        findObject(By.text("Next")).click()
        waitForIdle()
        /*
        - It can not find "Click to Grant Permission" for some weird reason
        - You gonna have to do it yourself unfortunately
        - It works sometimes, but fails some other times
        wait(Until.hasObject(By.text("Click to Grant Permission")), 10000)
        findObject(By.text("Click to Grant Permission")).click()
        findObject(By.text("Allow")).click()
        delayUi(1000)
        */
        Thread.sleep(3000) // You have 3 seconds to grant the permission
        findObject(By.text("Next")).click()
        Thread.sleep(1000)
        findObject(By.text("Next")).click()
        /*
        - It can not find "Click to Grant Permission" for some weird reason
        - You gonna have to do it yourself unfortunately
        - It works sometimes, but fails some other times
        delayUi(1000)
        findObject(By.text("Click to Grant Permission")).click()
        delayUi(3000)
        findObject(By.text("OK")).click()
        delayUi(10000)
        findObject(By.scrollable(true)).swipe(Direction.UP, .5f)
        delayUi(3000)
        findObject(By.text("Reluct")).click()
        delayUi(3000)
        findObject(By.checkable(true)).click()
         */
        Thread.sleep(10_000) // You have 20 seconds to grant Usage Access permission
        // After toggling to "Allowed", don't go back, just leave the screen as it is
        pressBack()
        pressBack()
        waitForIdle()

        findObject(By.text("Next")).click()

        //findObject(By.checkable(true)).click() // Toggle it yourself
        Thread.sleep(3000)
        findObject(By.text("Next")).click()
        waitForIdle()

        findObject(By.text("Next")).click()
        waitForIdle()

        findObject(By.text("Continue")).click()
        Thread.sleep(3_000)
    }
}

package work.racka.reluct.android.benchmark.util

import androidx.test.uiautomator.UiDevice

fun UiDevice.delayUi(timeMillis: Long) = waitForWindowUpdate(null, timeMillis)

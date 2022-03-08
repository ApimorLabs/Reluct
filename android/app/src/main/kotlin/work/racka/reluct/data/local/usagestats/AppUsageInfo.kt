package work.racka.reluct.data.local.usagestats

import android.graphics.drawable.Drawable

data class AppUsageInfo(
    val packageName: String,
    val appIcon: Drawable?,
    val dominantColor: Int,
    var timeInForeground: Long = 0,
    var appLaunchCount: Int = 0
)

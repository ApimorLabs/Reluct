package work.racka.reluct.common.features.screen_time.services

import android.content.Context
import android.content.Intent
import android.os.Build
import work.racka.reluct.common.features.screen_time.permissions.UsageAccessPermission

internal class AndroidScreenTimeServices(private val context: Context) : ScreenTimeServices {

    override fun startLimitsService() {
        val notification = UsageAccessPermission.requestUsageAccessNotification(context)
        if (UsageAccessPermission.isAllowed(context)) {
            notification.cancel()
            val intent = Intent(context, ScreenTimeLimitService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else context.startService(intent)
        } else {
            notification.show()
        }
    }

    override fun stopLimitsService() {
        val intent = Intent(context, ScreenTimeLimitService::class.java)
        context.stopService(intent)
    }
}
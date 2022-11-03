package work.racka.reluct.common.features.screenTime.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import org.koin.core.component.KoinComponent
import work.racka.reluct.common.features.screenTime.permissions.UsageAccessPermission

internal class RestartScreenTimeServiceReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent) {
        if (
            intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON" ||
            intent.action == "com.htc.intent.action.QUICKBOOT_POWERON"
        ) {
            val notification = UsageAccessPermission.requestUsageAccessNotification(context)
            if (UsageAccessPermission.isAllowed(context)) {
                notification.cancel()
                val service = Intent(context, ScreenTimeLimitService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(service)
                } else {
                    context.startService(service)
                }
            } else {
                notification.show()
            }
        }
    }
}

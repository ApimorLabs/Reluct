package work.racka.reluct.common.features.screen_time.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent

internal class RestartScreenTimeServiceReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent) {
        if (
            intent.action == Intent.ACTION_BOOT_COMPLETED
            || intent.action == "android.intent.action.QUICKBOOT_POWERON"
            || intent.action == "com.htc.intent.action.QUICKBOOT_POWERON"
        ) {
            AndroidScreenTimeServices(context).startLimitsService()
        }
    }
}
package work.racka.reluct.common.features.screen_time.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

internal class ScreenTimeLimitService : Service() {

    private var scope: CoroutineScope? = null

    override fun onCreate() {
        scope?.cancel()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        //super.onCreate()
    }

    override fun onDestroy() {
        scope?.cancel()
        super.onDestroy()
    }

    // Do Not Bind this service
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
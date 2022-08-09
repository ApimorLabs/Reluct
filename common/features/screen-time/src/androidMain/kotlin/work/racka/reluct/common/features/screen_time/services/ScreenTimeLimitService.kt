package work.racka.reluct.common.features.screen_time.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import kotlinx.coroutines.*

internal class ScreenTimeLimitService : Service() {

    private var scope: CoroutineScope? = null
    private var startServiceJob: Job? = null

    override fun onCreate() {
        scope?.cancel()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        //super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val context = this.applicationContext
        startServiceJob?.cancel()
        startServiceJob = scope?.launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "service starting. Is active: ${startServiceJob?.isActive}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return START_STICKY
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
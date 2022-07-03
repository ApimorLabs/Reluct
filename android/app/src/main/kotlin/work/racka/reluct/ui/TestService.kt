package work.racka.reluct.ui

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import work.racka.reluct.R

class TestService : Service() {
    private val flowN = callbackFlow<String> {
        var run = true
        var num = 1L
        while (run) {
            delay(2000)
            send("Number is now: ${num++}")
        }
        awaitClose {
            run = false
        }
    }.flowOn(Dispatchers.IO)

    private val scope = CoroutineScope(SupervisorJob())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = createNotification(applicationContext)
            notificationManager.createNotificationChannelGroup(
                NotificationChannelGroup("test", "Test Notif")
            )
            val notifChanel =
                NotificationChannel("service", "Service Notif", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notifChanel)
            startForeground(123, notification)
        }
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            flowN.collect {
                Timber.d("Service printing: $it")
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

object ServiceStarter {
    fun startService(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, TestService::class.java))
        } else {
            context.startService(Intent(context, TestService::class.java))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun createNotification(context: Context): Notification {
    val pendingIntent: PendingIntent =
        Intent(context, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(
                context, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

    return Notification.Builder(context, "service")
        .setContentTitle("Notif Title")
        .setContentText("Notification Text")
        .setSmallIcon(R.drawable.default_app_icon)
        .setContentIntent(pendingIntent)
        .setTicker("Ticker Text")
        .build()
}
package work.racka.reluct.common.features.screen_time.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import work.racka.reluct.common.core_navigation.compose_destinations.screentime.AppScreenTimeStatsDestination
import work.racka.reluct.common.data.usecases.app_usage.GetDailyAppUsageInfo
import work.racka.reluct.common.data.usecases.limits.GetAppLimits
import work.racka.reluct.common.features.screen_time.R
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats

internal class ScreenTimeLimitService : Service(), KoinComponent {

    private var scope: CoroutineScope? = null
    private var startServiceJob: Job? = null

    private val screenTimeServices: ScreenTimeServices by inject()
    private val getDailyAppUsageInfo: GetDailyAppUsageInfo by inject()
    private val getAppLimits: GetAppLimits by inject()

    override fun onCreate() {
        scope?.cancel()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        initializeService()
        //super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startServiceJob?.cancel()
        startServiceJob = scope?.launch {
            manageLimits()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        scope?.cancel()
        screenTimeServices.startLimitsService()
    }

    private fun initializeService() {
        val notification = ScreenTimeServiceNotification
            .createNotification(
                context = applicationContext,
                title = getString(R.string.screen_time_service_default_title),
                content = getString(R.string.screen_time_service_default_content),
                onNotificationClick = { null }
            )
        startForeground(ScreenTimeServiceNotification.NOTIFICATION_ID, notification)
    }

    private suspend fun manageLimits() {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        val currentStats = ScreenTimeDataProviders
            .getCurrentAppStats(getDailyAppUsageInfo, applicationContext)
        currentStats.collectLatest { stats ->
            val notification = appStatsNotification(applicationContext, stats)
            notificationManager.notify(ScreenTimeServiceNotification.NOTIFICATION_ID, notification)
        }
    }

    private fun appStatsNotification(context: Context, app: AppUsageStats): Notification {
        val uriString = AppScreenTimeStatsDestination
            .appScreenTimeDeepLink(app.appUsageInfo.packageName)
        val intent = Intent(
            Intent.ACTION_VIEW,
            uriString.toUri()
        )
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return ScreenTimeServiceNotification
            .createNotification(
                context = applicationContext,
                title = getString(R.string.current_app_arg, app.appUsageInfo.appName),
                content = getString(
                    R.string.current_app_time_arg,
                    app.appUsageInfo.formattedTimeInForeground
                ),
                onNotificationClick = { pendingIntent }
            )
    }

    // Do Not Bind this service
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
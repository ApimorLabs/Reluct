package work.racka.reluct.common.features.screen_time.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import work.racka.reluct.common.core_navigation.compose_destinations.screentime.AppScreenTimeStatsDestination
import work.racka.reluct.common.data.usecases.app_usage.GetAppUsageInfo
import work.racka.reluct.common.data.usecases.limits.GetAppLimits
import work.racka.reluct.common.data.usecases.limits.ManageFocusMode
import work.racka.reluct.common.features.screen_time.R
import work.racka.reluct.common.features.screen_time.permissions.UsageAccessPermission
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats

internal class AndroidScreenTimeServices(
    private val context: Context,
    private val getAppUsageInfo: GetAppUsageInfo,
    private val getAppLimits: GetAppLimits,
    private val manageFocusMode: ManageFocusMode
) : ScreenTimeServices {

    override suspend fun startLimitsService() {
        val notification = UsageAccessPermission.requestUsageAccessNotification(context)
        val isAppBlockingEnabled = manageFocusMode.isAppBlockingEnabled.firstOrNull()
        if (UsageAccessPermission.isAllowed(context) && isAppBlockingEnabled == true) {
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeCurrentAppBlocking(): Flow<ScreenTimeServices.BlockState> {
        val notificationManager = NotificationManagerCompat.from(context)
        return ScreenTimeDataProviders
            .getCurrentAppStats(getAppUsageInfo, context).mapLatest { stats ->
                // Update Our Notification
                val notification = appStatsNotification(context, stats)
                notificationManager.notify(
                    ScreenTimeServiceNotification.NOTIFICATION_ID,
                    notification
                )

                val currentDuration = stats.appUsageInfo.timeInForeground
                val isFocusModeOn = manageFocusMode.isFocusModeOn.first()
                val appLimits = getAppLimits.getAppSync(stats.appUsageInfo.packageName)
                val appPastLimit =
                    (currentDuration >= appLimits.timeLimit && appLimits.timeLimit != 0L)


                // Check if the app doesn't violate limits
                // Check if the app doesn't violate limits
                val limitParamChecks = (isFocusModeOn && appLimits.isADistractingAp)
                        || appLimits.isPaused || appPastLimit
                if (!appLimits.overridden && limitParamChecks) {
                    ScreenTimeServices.BlockState.Blocked(appLimits.appInfo.packageName)
                } else {
                    ScreenTimeServices.BlockState.Allowed(appLimits.appInfo.packageName)
                }
            }
            .flowOn(Dispatchers.IO)
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
                context = context,
                title = context.getString(R.string.current_app_arg, app.appUsageInfo.appName),
                content = context.getString(
                    R.string.current_app_time_arg,
                    app.appUsageInfo.formattedTimeInForeground
                ),
                onNotificationClick = { pendingIntent }
            )
    }
}
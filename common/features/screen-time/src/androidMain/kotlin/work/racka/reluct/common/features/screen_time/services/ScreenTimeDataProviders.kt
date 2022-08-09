package work.racka.reluct.common.features.screen_time.services

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.os.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock
import work.racka.reluct.common.data.usecases.app_usage.GetDailyAppUsageInfo
import work.racka.reluct.common.features.screen_time.permissions.checkUsageAccessPermissions
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.WeekUtils

internal object ScreenTimeDataProviders {
    fun getCurrentAppStats(
        getDailyAppUsageInfo: GetDailyAppUsageInfo,
        applicationContext: Context
    ): Flow<AppUsageStats> = flow {
        while (true) {
            delay(500)
            val currentTime = Clock.System.now().toEpochMilliseconds()
            val startTimeMillis = currentTime - (1000 * 1800)
            if (checkUsageAccessPermissions(applicationContext)) {
                var latestEvent: UsageEvents.Event? = null

                val usageStats = applicationContext
                    .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                val usageEvents = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val userManager = applicationContext
                        .getSystemService(Context.USER_SERVICE) as UserManager
                    if (userManager.isUserUnlocked) usageStats
                        .queryEvents(startTimeMillis, currentTime)
                    else null
                } else usageStats.queryEvents(startTimeMillis, currentTime)

                while (usageEvents?.hasNextEvent() == true) {
                    val currentEvent = UsageEvents.Event()
                    usageEvents.getNextEvent(currentEvent)
                    if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                        latestEvent = currentEvent
                    }
                }
                latestEvent?.let { event ->
                    getDailyAppUsageInfo.invoke(
                        dayIsoNumber = WeekUtils.currentDayOfWeek().isoDayNumber,
                        packageName = event.packageName
                    )
                }?.let { app -> emit(app) } // Emitting the stats for the app.
            } else {
                // Show Missing Permission Notification
            }
        }
    }.flowOn(Dispatchers.IO)
}
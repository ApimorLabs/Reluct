package work.racka.reluct.common.features.screen_time.services

import android.app.KeyguardManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
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
            if (checkUsageAccessPermissions(applicationContext)) {
                val currentTime = Clock.System.now().toEpochMilliseconds()
                val startTimeMillis = currentTime - (1000 * 1800)
                var latestEvent: UsageEvents.Event? = null

                val keyguardManager = applicationContext
                    .getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                val usageStats = applicationContext
                    .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                val usageEvents = if (!keyguardManager.isKeyguardLocked) usageStats
                    .queryEvents(startTimeMillis, currentTime) else null

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
                // TODO: Show Missing Permission Notification
            }
        }
    }.flowOn(Dispatchers.IO)
}
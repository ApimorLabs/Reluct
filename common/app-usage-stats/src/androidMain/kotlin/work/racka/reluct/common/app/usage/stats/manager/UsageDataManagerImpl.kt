package work.racka.reluct.common.app.usage.stats.manager

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.os.UserManager
import work.racka.reluct.common.app.usage.stats.model.DataAppUsageInfo
import work.racka.reluct.common.app.usage.stats.model.DataUsageStats
import work.racka.reluct.common.app.usage.stats.util.sortByHighestForegroundTime

internal class UsageDataManagerImpl(
    private val context: Context,
    private val usageStats: UsageStatsManager
) : UsageDataManager {
    /**
     * Returns DataUsageStats that contains a list of DataAppUsageInfo sorted by
     * timeInForeground descending
     *
     * For Android:
     * Can only provide data for about two weeks (offsetWeekBy >= -1)
     * The Android system does not store Usage Events for more than two weeks
     * You can use UsageStatsManager.queryUsageStats to get more data but that is not very
     * accurate and can deviate a lot from the actual usage statistics
     */
    override suspend fun getUsageStats(startTimeMillis: Long, endTimeMillis: Long): DataUsageStats {
        val allEvents = mutableListOf<UsageEvents.Event>()
        // The key is the package name for the app
        val appUsageInfoMap = hashMapOf<String, DataAppUsageInfo>()
        var unlockCount = 0L

        val usageEvents = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
            if (userManager.isUserUnlocked) usageStats.queryEvents(startTimeMillis, endTimeMillis)
            else null
        } else usageStats.queryEvents(startTimeMillis, endTimeMillis)

        while (usageEvents?.hasNextEvent() == true) {
            val currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)

            if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
                currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED
            ) {
                allEvents.add(currentEvent)
                // key is packageName
                val key = currentEvent.packageName
                // taking it into a collection to access by package name
                if (appUsageInfoMap[key] == null) {
                    appUsageInfoMap[key] = DataAppUsageInfo(packageName = key)
                }
            }

            if (currentEvent.eventType == UsageEvents.Event.KEYGUARD_HIDDEN) {
                unlockCount++
            }
        }

        for (i in 0 until allEvents.lastIndex) {
            val e0 = allEvents[i]
            val e1 = allEvents[i + 1]

            // For UsageTime of apps in time range
            if (e0.eventType == UsageEvents.Event.ACTIVITY_RESUMED &&
                e1.eventType == UsageEvents.Event.ACTIVITY_PAUSED &&
                e0.className == e1.className
            ) {
                val diff = e1.timeStamp - e0.timeStamp
                appUsageInfoMap[e0.packageName]!!.timeInForeground += diff
            }

            // For App launch count
            if (e0.packageName != e1.packageName &&
                e1.eventType == UsageEvents.Event.ACTIVITY_RESUMED
            ) {
                // if true, E1 (launch event of an app) app launched
                appUsageInfoMap[e1.packageName]!!.appLaunchCount++
            }
        }
        val appUsageInfoList = appUsageInfoMap.values.sortByHighestForegroundTime()
        return DataUsageStats(
            appsUsageList = appUsageInfoList,
            unlockCount = unlockCount
        )
    }

    override suspend fun getAppUsage(
        startTimeMillis: Long,
        endTimeMillis: Long,
        packageName: String
    ): DataAppUsageInfo {
        val allAppsData = getUsageStats(startTimeMillis, endTimeMillis)
        return allAppsData.appsUsageList.first { it.packageName == packageName }
    }
}
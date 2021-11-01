package work.racka.reluct.data.local.usagestats

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import timber.log.Timber
import work.racka.reluct.model.UsageStats
import work.racka.reluct.utils.Utils
import java.util.*

class UsageDataManager(
    private val context: Context,
    private val usageStatsManager: UsageStatsManager
) {

    /**
     * Can only provide data for about two weeks (offsetWeekBy >= -1)
     * The Android system does not store Usage Events for more than two weeks
     * You can use UsageStatsManager.queryUsageStats to get more data but that is not very
     * accurate and can deviate a lot from the actual usage statistics
     */
    fun getWeeklyUsage(offsetWeekBy: Int): List<UsageStats> {
        val daysOfTheWeek = Week.values()
        val usageStats = mutableListOf<UsageStats>()
        var startTime: Calendar
        var endTime: Calendar

        for (dayOfWeek in daysOfTheWeek) {
            val weekOffset = if (dayOfWeek == Week.SUNDAY) offsetWeekBy + 1 else offsetWeekBy
            Timber.d("Week Day: ${dayOfWeek.day}")
            startTime = startTime(dayOfWeek.value, offsetWeekBy)
            endTime = endTime(dayOfWeek.value, offsetWeekBy)

            val date = Utils.getFormattedDate(startTime.time)
            val appsUsageList = getUsageStatistics(startTime.timeInMillis, endTime.timeInMillis)
            val totalScreenTime = appsUsageList.sumOf { data ->
                data.timeInForeground
            }

            Timber.d("Screen Time for $date: ${Utils.getFormattedTime(totalScreenTime)}")
            val stats = UsageStats(appsUsageList, dayOfWeek, date, totalScreenTime)
            usageStats.add(stats)
        }

        Timber.d("Usage Stats count: ${usageStats.size}")
        return usageStats
    }

    /**
     * Returns a list of AppUsageInfo that is sorted by timeInForeground descending
     */
    private fun getUsageStatistics(startTime: Long, endTime: Long): List<AppUsageInfo> {
        val allEvents = mutableListOf<UsageEvents.Event>()
        val map = hashMapOf<String, AppUsageInfo>()

        val usageEvents = usageStatsManager.queryEvents(startTime, endTime)

        while (usageEvents.hasNextEvent()) {
            val currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
                currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED
            ) {
                allEvents.add(currentEvent)
                val key = currentEvent.packageName
                // taking it into a collection to access by package name
                if (map[key] == null) {
                    val appIcon = Utils.getAppIcon(key, context)
                    val dominantColor = Utils.getDominantAppIconColor(appIcon)
                    map[key] = AppUsageInfo(
                        key,
                        appIcon,
                        dominantColor
                    )
                }
            }
        }

        for (i in 0 until allEvents.size - 1) {
            val e0 = allEvents[i]
            val e1 = allEvents[i + 1]

            // For UsageTime of apps in time range
            if (e0.eventType == UsageEvents.Event.ACTIVITY_RESUMED &&
                e1.eventType == UsageEvents.Event.ACTIVITY_PAUSED &&
                e0.className == e1.className
            ) {
                val diff = e1.timeStamp - e0.timeStamp
                map[e0.packageName]!!.timeInForeground += diff
            }

            // For App launch count
            if (e0.packageName != e1.packageName &&
                e1.eventType == UsageEvents.Event.ACTIVITY_RESUMED
            ) {
                // if true, E1 (launch event of an app) app launched
                map[e1.packageName]!!.appLaunchCount += 1
            }
        }

        return Utils.sortByHighestForegroundTime(map.values)
    }

    /**
     * Sets the start and end time of a provided day starting from the current week
     * Week can be offset with the offsetWeekBy parameter
     * Positive Offset means next week
     * Negative Offset means previous week
     */
    private fun startTime(day: Int, offsetWeekBy: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.WEEK_OF_YEAR, offsetWeekBy)
            set(Calendar.DAY_OF_WEEK, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar
    }

    private fun endTime(day: Int, offsetWeekBy: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.WEEK_OF_YEAR, offsetWeekBy)
            set(Calendar.DAY_OF_WEEK, day)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar
    }

}
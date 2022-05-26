package work.racka.reluct.common.app.usage.stats.manager

import work.racka.reluct.common.app.usage.stats.model.DataUsageStats

interface UsageDataManager {
    /**
     * Returns DataUsageStats that contains a list of DataAppUsageInfo sorted by
     * timeInForeground descending
     */
    fun getUsageStats(startTimeMillis: Long, endTimeMillis: Long): DataUsageStats
}
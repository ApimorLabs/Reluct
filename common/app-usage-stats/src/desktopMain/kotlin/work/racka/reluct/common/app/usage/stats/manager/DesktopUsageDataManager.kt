package work.racka.reluct.common.app.usage.stats.manager

import work.racka.reluct.common.app.usage.stats.model.DataAppUsageInfo
import work.racka.reluct.common.app.usage.stats.model.DataUsageStats

internal class DesktopUsageDataManager : UsageDataManager {

    override suspend fun getUsageStats(startTimeMillis: Long, endTimeMillis: Long): DataUsageStats {
        return DataUsageStats(emptyList(), 0)
    }

    override suspend fun getAppUsage(
        startTimeMillis: Long,
        endTimeMillis: Long,
        packageName: String
    ): DataAppUsageInfo {
        return DataAppUsageInfo(packageName, 0, 0)
    }
}

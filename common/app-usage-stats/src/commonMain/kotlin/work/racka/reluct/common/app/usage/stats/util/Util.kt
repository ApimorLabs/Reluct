package work.racka.reluct.common.app.usage.stats.util

import work.racka.reluct.common.app.usage.stats.model.DataAppUsageInfo

fun Collection<DataAppUsageInfo>.sortByHighestForegroundTime(): List<DataAppUsageInfo> {
    return this.sortedByDescending { appInfo ->
        appInfo.timeInForeground
    }
}
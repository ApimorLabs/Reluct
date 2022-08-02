package work.racka.reluct.common.model.domain.limits

import work.racka.reluct.common.model.domain.app_info.AppInfo

data class AppTimeLimit(
    val appInfo: AppInfo,
    val timeInMillis: Long,
    val hours: Int,
    val minutes: Int,
    val formattedTime: String
)

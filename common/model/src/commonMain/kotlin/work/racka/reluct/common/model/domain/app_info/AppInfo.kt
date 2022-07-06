package work.racka.reluct.common.model.domain.app_info

import work.racka.reluct.common.model.domain.usagestats.Icon

data class AppInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Icon,
)

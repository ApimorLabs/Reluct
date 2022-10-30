package work.racka.reluct.common.model.domain.appInfo

import work.racka.reluct.common.model.domain.core.Icon

data class AppInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Icon,
)

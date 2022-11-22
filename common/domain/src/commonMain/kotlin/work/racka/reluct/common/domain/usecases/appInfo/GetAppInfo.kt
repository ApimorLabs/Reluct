package work.racka.reluct.common.domain.usecases.appInfo

import work.racka.reluct.common.model.domain.core.Icon

interface GetAppInfo {
    suspend fun getAppIcon(packageName: String): Icon
    suspend fun getAppName(packageName: String): String
}

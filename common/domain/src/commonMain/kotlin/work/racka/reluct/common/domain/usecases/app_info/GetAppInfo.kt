package work.racka.reluct.common.domain.usecases.app_info

import work.racka.reluct.common.model.domain.core.Icon

interface GetAppInfo {
    suspend fun getAppIcon(packageName: String): Icon
    suspend fun getAppName(packageName: String): String
}
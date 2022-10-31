package work.racka.reluct.common.domain.usecases.app_info

import work.racka.reluct.common.model.domain.appInfo.AppInfo

interface GetInstalledApps {
    suspend fun invoke(): List<AppInfo>
}
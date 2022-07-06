package work.racka.reluct.common.data.usecases.app_info

import work.racka.reluct.common.model.domain.app_info.AppInfo

interface GetInstalledApps {
    suspend operator fun invoke(): List<AppInfo>
}
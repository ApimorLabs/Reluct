package work.racka.reluct.common.data.usecases.app_info

import work.racka.reluct.common.model.domain.app_info.AppInfo

class DesktopGetInstalledApps : GetInstalledApps {
    override suspend fun invoke(): List<AppInfo> {
        return emptyList()
    }
}
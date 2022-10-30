package work.racka.reluct.common.domain.usecases.app_info

import work.racka.reluct.common.model.domain.appInfo.AppInfo

internal class DesktopGetInstalledApps : GetInstalledApps {
    override suspend fun invoke(): List<AppInfo> {
        return emptyList()
    }
}
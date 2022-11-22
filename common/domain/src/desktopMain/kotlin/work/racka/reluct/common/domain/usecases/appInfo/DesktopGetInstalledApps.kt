package work.racka.reluct.common.domain.usecases.appInfo

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import work.racka.reluct.common.model.domain.appInfo.AppInfo

internal class DesktopGetInstalledApps : GetInstalledApps {
    override suspend fun invoke(): ImmutableList<AppInfo> {
        return persistentListOf()
    }
}

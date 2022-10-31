package work.racka.reluct.common.domain.usecases.app_info

import kotlinx.collections.immutable.ImmutableList
import work.racka.reluct.common.model.domain.appInfo.AppInfo

interface GetInstalledApps {
    suspend fun invoke(): ImmutableList<AppInfo>
}
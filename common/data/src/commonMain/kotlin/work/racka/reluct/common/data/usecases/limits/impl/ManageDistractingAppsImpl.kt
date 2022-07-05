package work.racka.reluct.common.data.usecases.limits.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import work.racka.reluct.common.data.usecases.app_info.GetInstalledApps
import work.racka.reluct.common.data.usecases.limits.GetDistractingApps
import work.racka.reluct.common.data.usecases.limits.ManageDistractingApps
import work.racka.reluct.common.data.usecases.limits.ModifyAppLimits
import work.racka.reluct.common.model.domain.app_info.AppInfo

class ManageDistractingAppsImpl(
    private val getDistractingApps: GetDistractingApps,
    private val getInstalledApps: GetInstalledApps,
    private val modifyAppLimits: ModifyAppLimits,
    private val backgroundDispatcher: CoroutineDispatcher
) : ManageDistractingApps {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun invoke(): Flow<Pair<List<AppInfo>, List<AppInfo>>> {
        val installedApps = getInstalledApps()
        return getDistractingApps().mapLatest { distractingApps ->
            val distractingAppsInfo = distractingApps.map { it.appInfo }
            val nonDistractingApps = installedApps - distractingAppsInfo.toSet()
            Pair(first = distractingAppsInfo, second = nonDistractingApps)
        }.flowOn(backgroundDispatcher)
    }

    override suspend fun markAsDistracting(packageName: String) {
        modifyAppLimits.makeDistractingApp(packageName = packageName, isDistracting = true)
    }

    override suspend fun markAsNotDistracting(packageName: String) {
        modifyAppLimits.makeDistractingApp(packageName = packageName, isDistracting = false)
    }
}
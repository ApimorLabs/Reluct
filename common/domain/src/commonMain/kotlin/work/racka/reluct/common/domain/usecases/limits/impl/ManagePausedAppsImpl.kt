package work.racka.reluct.common.domain.usecases.limits.impl

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import work.racka.reluct.common.domain.usecases.app_info.GetInstalledApps
import work.racka.reluct.common.domain.usecases.limits.GetPausedApps
import work.racka.reluct.common.domain.usecases.limits.ManagePausedApps
import work.racka.reluct.common.domain.usecases.limits.ModifyAppLimits
import work.racka.reluct.common.model.domain.appInfo.AppInfo
import work.racka.reluct.common.model.util.list.filterPersistentNot
import work.racka.reluct.common.services.haptics.HapticFeedback

internal class ManagePausedAppsImpl(
    private val getPausedApps: GetPausedApps,
    private val getInstalledApps: GetInstalledApps,
    private val modifyAppLimits: ModifyAppLimits,
    private val haptics: HapticFeedback,
    private val backgroundDispatcher: CoroutineDispatcher
) : ManagePausedApps {

    private var installedApps: ImmutableList<AppInfo> = persistentListOf()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getApps(): Flow<Pair<ImmutableList<AppInfo>, ImmutableList<AppInfo>>> =
        getPausedApps.getApps().mapLatest { pausedApps ->
            if (installedApps.isEmpty()) installedApps = getInstalledApps.invoke()
            val pausedAppsInfo = pausedApps.map { it.appInfo }.toImmutableList()
            val nonPausedApps = installedApps.filterPersistentNot { installed ->
                pausedAppsInfo.any { it.packageName == installed.packageName }
            }
            Pair(first = pausedAppsInfo, second = nonPausedApps)
        }.flowOn(backgroundDispatcher)

    override suspend fun pauseApp(packageName: String) {
        modifyAppLimits.pauseApp(packageName = packageName, isPaused = true)
        haptics.tick()
    }

    override suspend fun unPauseApp(packageName: String) {
        modifyAppLimits.pauseApp(packageName = packageName, isPaused = false)
        haptics.tick()
    }

    override suspend fun isPaused(packageName: String): Boolean =
        getPausedApps.isPaused(packageName)
}
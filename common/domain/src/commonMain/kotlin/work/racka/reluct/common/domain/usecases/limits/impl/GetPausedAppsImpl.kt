package work.racka.reluct.common.domain.usecases.limits.impl

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.screentime.LimitsDao
import work.racka.reluct.common.domain.mappers.limits.asAppLimits
import work.racka.reluct.common.domain.usecases.app_info.GetAppInfo
import work.racka.reluct.common.domain.usecases.limits.GetPausedApps
import work.racka.reluct.common.model.domain.limits.AppLimits

internal class GetPausedAppsImpl(
    private val limitsDao: LimitsDao,
    private val getAppInfo: GetAppInfo,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetPausedApps {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getApps(): Flow<ImmutableList<AppLimits>> = limitsDao.getPausedApps()
        .mapLatest { list ->
            list.map { dbAppLimits -> dbAppLimits.asAppLimits(getAppInfo) }
                .sortedBy { appLimits -> appLimits.appInfo.appName }
                .toImmutableList()
        }.flowOn(backgroundDispatcher)

    override suspend fun getSync(): ImmutableList<AppLimits> = withContext(backgroundDispatcher) {
        limitsDao.getPausedAppsSync().map {
            it.asAppLimits(getAppInfo)
        }.sortedBy { it.appInfo.appName }
            .toImmutableList()
    }

    override suspend fun isPaused(packageName: String): Boolean =
        withContext(backgroundDispatcher) {
            /** Don't check here **/
            /*val app = limitsDao.getAppSync(packageName)
            val screenTimeExceeded =
                if (app.timeLimit == 0L) false else currentUsage > app.timeLimit
            if (screenTimeExceeded) limitsDao.togglePausedApp(packageName, true)
            if (app.overridden) false
            else */
            limitsDao.isAppPaused(packageName)
        }
}
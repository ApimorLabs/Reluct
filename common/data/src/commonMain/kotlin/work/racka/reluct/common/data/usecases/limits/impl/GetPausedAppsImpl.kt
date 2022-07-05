package work.racka.reluct.common.data.usecases.limits.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import work.racka.reluct.common.data.mappers.limits.asAppLimits
import work.racka.reluct.common.data.usecases.app_info.GetAppInfo
import work.racka.reluct.common.data.usecases.limits.GetPausedApps
import work.racka.reluct.common.database.dao.screentime.LimitsDao
import work.racka.reluct.common.model.domain.limits.AppLimits

class GetPausedAppsImpl(
    private val limitsDao: LimitsDao,
    private val getAppInfo: GetAppInfo,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetPausedApps {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun invoke(): Flow<List<AppLimits>> = limitsDao.getPausedApps()
        .mapLatest { list ->
            list.map { dbAppLimits -> dbAppLimits.asAppLimits(getAppInfo) }
                .sortedBy { appLimits -> appLimits.appInfo.appName }
        }
        .flowOn(backgroundDispatcher)

    override suspend fun getSync(): List<AppLimits> = withContext(backgroundDispatcher) {
        limitsDao.getPausedAppsSync().map {
            it.asAppLimits(getAppInfo)
        }.sortedBy { it.appInfo.appName }
    }

    override suspend fun isPaused(packageName: String, currentUsage: Long): Boolean {
        val app = limitsDao.getAppSync(packageName)
        val screenTimeExceeded = if (app.timeLimit == 0L) false else currentUsage > app.timeLimit
        if (screenTimeExceeded) limitsDao.togglePausedApp(packageName, true)
        return if (app.overridden) false
        else limitsDao.isAppPaused(packageName)
    }
}
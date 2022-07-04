package work.racka.reluct.common.data.usecases.limits.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import work.racka.reluct.common.data.mappers.limits.asAppLimits
import work.racka.reluct.common.data.usecases.limits.GetPausedApps
import work.racka.reluct.common.database.dao.screentime.LimitsDao
import work.racka.reluct.common.model.domain.limits.AppLimits

class GetPausedAppsImpl(
    private val limitsDao: LimitsDao,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetPausedApps {

    override suspend fun invoke(): Flow<List<AppLimits>> = limitsDao.getPausedApps()
        .map { list -> list.map { it.asAppLimits() } }
        .flowOn(backgroundDispatcher)

    override suspend fun getSync(): List<AppLimits> = withContext(backgroundDispatcher) {
        limitsDao.getPausedAppsSync().map {
            it.asAppLimits()
        }
    }

    override suspend fun isPaused(packageName: String, currentUsage: Long): Boolean {
        val app = limitsDao.getAppSync(packageName)
        val screenTimeExceeded = if (app.timeLimit == 0L) false else currentUsage > app.timeLimit
        if (screenTimeExceeded) limitsDao.togglePausedApp(packageName, true)
        return if (app.overridden) false
        else limitsDao.isAppPaused(packageName)
    }
}
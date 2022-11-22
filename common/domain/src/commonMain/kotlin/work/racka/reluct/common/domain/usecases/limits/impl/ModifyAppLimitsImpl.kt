package work.racka.reluct.common.domain.usecases.limits.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.screentime.LimitsDao
import work.racka.reluct.common.domain.mappers.limits.asLimitsDbObject
import work.racka.reluct.common.domain.usecases.limits.ModifyAppLimits
import work.racka.reluct.common.model.domain.limits.AppLimits

internal class ModifyAppLimitsImpl(
    private val limitsDao: LimitsDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ModifyAppLimits {

    override suspend fun insertApp(appLimits: AppLimits) = withContext(dispatcher) {
        limitsDao.insertApp(appLimits.asLimitsDbObject())
    }

    override suspend fun removeApp(packageName: String) = withContext(dispatcher) {
        limitsDao.removeApp(packageName)
    }

    override suspend fun removeAllApp(packageName: String) = withContext(dispatcher) {
        limitsDao.removeAllApps()
    }

    override suspend fun resumeAllApps() = withContext(dispatcher) {
        limitsDao.resumeAllApps()
    }

    override suspend fun setLimit(packageName: String, timeLimit: Long) = withContext(dispatcher) {
        limitsDao.setTimeLimit(packageName, timeLimit)
    }

    override suspend fun pauseApp(packageName: String, isPaused: Boolean) =
        withContext(dispatcher) {
            limitsDao.togglePausedApp(packageName, isPaused)
        }

    override suspend fun makeDistractingApp(packageName: String, isDistracting: Boolean) =
        withContext(dispatcher) {
            limitsDao.toggleDistractingApp(packageName, isDistracting)
        }

    override suspend fun overrideLimit(packageName: String, overridden: Boolean) =
        withContext(dispatcher) {
            limitsDao.toggleLimitOverride(packageName, overridden)
        }
}

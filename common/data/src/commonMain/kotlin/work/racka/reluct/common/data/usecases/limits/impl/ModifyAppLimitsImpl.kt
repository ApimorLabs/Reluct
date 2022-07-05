package work.racka.reluct.common.data.usecases.limits.impl

import work.racka.reluct.common.data.mappers.limits.asLimitsDbObject
import work.racka.reluct.common.data.usecases.limits.ModifyAppLimits
import work.racka.reluct.common.database.dao.screentime.LimitsDao
import work.racka.reluct.common.model.domain.limits.AppLimits

internal class ModifyAppLimitsImpl(
    private val limitsDao: LimitsDao
) : ModifyAppLimits {

    override suspend fun insertApp(appLimits: AppLimits) {
        limitsDao.insertApp(appLimits.asLimitsDbObject())
    }

    override suspend fun removeApp(packageName: String) {
        limitsDao.removeApp(packageName)
    }

    override suspend fun removeAllApp(packageName: String) {
        limitsDao.removeAllApps()
    }

    override suspend fun resumeAllApps() {
        limitsDao.resumeAllApps()
    }

    override suspend fun setLimit(packageName: String, timeLimit: Long) {
        limitsDao.setTimeLimit(packageName, timeLimit)
    }

    override suspend fun pauseApp(packageName: String, isPaused: Boolean) {
        limitsDao.togglePausedApp(packageName, isPaused)
    }

    override suspend fun makeDistractingApp(packageName: String, isDistracting: Boolean) {
        limitsDao.toggleDistractingApp(packageName, isDistracting)
    }

    override suspend fun overrideLimit(packageName: String, overridden: Boolean) {
        limitsDao.toggleLimitOverride(packageName, overridden)
    }
}
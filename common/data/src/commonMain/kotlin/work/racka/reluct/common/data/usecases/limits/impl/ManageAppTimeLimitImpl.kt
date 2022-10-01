package work.racka.reluct.common.data.usecases.limits.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import work.racka.reluct.common.data.mappers.limits.asTimeLimit
import work.racka.reluct.common.data.mappers.limits.convertTimeToMillis
import work.racka.reluct.common.data.usecases.app_info.GetAppInfo
import work.racka.reluct.common.data.usecases.limits.ManageAppTimeLimit
import work.racka.reluct.common.database.dao.screentime.LimitsDao
import work.racka.reluct.common.model.domain.limits.AppTimeLimit
import work.racka.reluct.common.system_service.haptics.HapticFeedback

internal class ManageAppTimeLimitImpl(
    private val limitsDao: LimitsDao,
    private val getAppInfo: GetAppInfo,
    private val haptics: HapticFeedback,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ManageAppTimeLimit {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(packageName: String): Flow<AppTimeLimit> =
        limitsDao.getApp(packageName).mapLatest { it.asTimeLimit(getAppInfo) }
            .flowOn(dispatcher)

    override suspend fun getSync(packageName: String): AppTimeLimit = withContext(dispatcher) {
        limitsDao.getAppSync(packageName).asTimeLimit(getAppInfo)
    }

    override suspend fun setTimeLimit(appTimeLimit: AppTimeLimit) = withContext(dispatcher) {
        haptics.tick()
        val timeInMillis =
            convertTimeToMillis(hours = appTimeLimit.hours, minutes = appTimeLimit.minutes)
        limitsDao.setTimeLimit(packageName = appTimeLimit.appInfo.packageName, timeInMillis)
    }
}
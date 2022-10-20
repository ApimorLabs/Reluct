package work.racka.reluct.common.domain.usecases.limits.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.screentime.LimitsDao
import work.racka.reluct.common.domain.mappers.limits.asAppLimits
import work.racka.reluct.common.domain.usecases.app_info.GetAppInfo
import work.racka.reluct.common.domain.usecases.limits.GetAppLimits
import work.racka.reluct.common.model.domain.limits.AppLimits

internal class GetAppLimitsImpl(
    private val limitsDao: LimitsDao,
    private val getAppInfo: GetAppInfo,
    private val dispatcher: CoroutineDispatcher
) : GetAppLimits {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getApp(packageName: String): Flow<AppLimits> =
        limitsDao.getApp(packageName).mapLatest { it.asAppLimits(getAppInfo) }
            .flowOn(dispatcher)

    override suspend fun getAppSync(packageName: String): AppLimits = withContext(dispatcher) {
        limitsDao.getAppSync(packageName).asAppLimits(getAppInfo)
    }
}
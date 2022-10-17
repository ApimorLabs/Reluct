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
import work.racka.reluct.common.domain.usecases.limits.GetDistractingApps
import work.racka.reluct.common.model.domain.limits.AppLimits

internal class GetDistractingAppsImpl(
    private val limitsDao: LimitsDao,
    private val getAppInfo: GetAppInfo,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetDistractingApps {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(): Flow<List<AppLimits>> = limitsDao.getDistractingApps()
        .mapLatest { list ->
            list.map { dbAppLimits -> dbAppLimits.asAppLimits(getAppInfo) }
                .sortedBy { appLimits -> appLimits.appInfo.appName }
        }.flowOn(backgroundDispatcher)

    override suspend fun getSync(): List<AppLimits> = withContext(backgroundDispatcher) {
        limitsDao.getDistractingAppsSync().map { list -> list.asAppLimits(getAppInfo) }
            .sortedBy { appLimits -> appLimits.appInfo.appName }
    }

    override suspend fun isDistractingApp(packageName: String): Boolean =
        withContext(backgroundDispatcher) {
            limitsDao.isDistractingApp(packageName)
        }
}
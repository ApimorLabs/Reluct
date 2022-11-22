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
import work.racka.reluct.common.domain.usecases.appInfo.GetAppInfo
import work.racka.reluct.common.domain.usecases.limits.GetDistractingApps
import work.racka.reluct.common.model.domain.limits.AppLimits

internal class GetDistractingAppsImpl(
    private val limitsDao: LimitsDao,
    private val getAppInfo: GetAppInfo,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetDistractingApps {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getApps(): Flow<ImmutableList<AppLimits>> = limitsDao.getDistractingApps()
        .mapLatest { list ->
            list.map { dbAppLimits -> dbAppLimits.asAppLimits(getAppInfo) }
                .sortedBy { appLimits -> appLimits.appInfo.appName }
                .toImmutableList()
        }.flowOn(backgroundDispatcher)

    override suspend fun getSync(): ImmutableList<AppLimits> = withContext(backgroundDispatcher) {
        limitsDao.getDistractingAppsSync().map { list -> list.asAppLimits(getAppInfo) }
            .sortedBy { appLimits -> appLimits.appInfo.appName }
            .toImmutableList()
    }

    override suspend fun isDistractingApp(packageName: String): Boolean =
        withContext(backgroundDispatcher) {
            limitsDao.isDistractingApp(packageName)
        }
}

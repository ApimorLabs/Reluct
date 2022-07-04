package work.racka.reluct.common.data.usecases.limits.impl

import kotlinx.coroutines.CoroutineDispatcher
import work.racka.reluct.common.data.usecases.limits.GetPausedApps
import work.racka.reluct.common.database.dao.screentime.LimitsDao

class GetPausedAppsImpl(
    limitsDao: LimitsDao,
    backgroundDispatcher: CoroutineDispatcher
) : GetPausedApps {
    override suspend fun invoke() {
        TODO("Not yet implemented")
    }

    override suspend fun getSync() {
        TODO("Not yet implemented")
    }
}
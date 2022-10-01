package work.racka.reluct.common.data.usecases.limits

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.limits.AppLimits

interface GetPausedApps {
    fun invoke(): Flow<List<AppLimits>>

    suspend fun getSync(): List<AppLimits>

    suspend fun isPaused(packageName: String): Boolean
}
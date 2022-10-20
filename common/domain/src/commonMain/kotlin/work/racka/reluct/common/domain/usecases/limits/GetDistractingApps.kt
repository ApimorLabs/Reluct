package work.racka.reluct.common.domain.usecases.limits

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.limits.AppLimits

interface GetDistractingApps {
    fun invoke(): Flow<List<AppLimits>>

    suspend fun getSync(): List<AppLimits>

    suspend fun isDistractingApp(packageName: String): Boolean
}
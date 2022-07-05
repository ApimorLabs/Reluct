package work.racka.reluct.common.data.usecases.limits

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.limits.AppLimits

interface GetDistractingApps {
    suspend operator fun invoke(): Flow<List<AppLimits>>

    suspend fun getSync(): List<AppLimits>

    suspend fun isDistractingApp(packageName: String): Boolean
}
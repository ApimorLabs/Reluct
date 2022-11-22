package work.racka.reluct.common.domain.usecases.limits

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.limits.AppLimits

interface GetDistractingApps {
    fun getApps(): Flow<ImmutableList<AppLimits>>

    suspend fun getSync(): ImmutableList<AppLimits>

    suspend fun isDistractingApp(packageName: String): Boolean
}

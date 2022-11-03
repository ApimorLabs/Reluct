package work.racka.reluct.common.domain.usecases.limits

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.limits.AppTimeLimit

interface ManageAppTimeLimit {
    /**
     * Get the specified app packageName time Limit as a Flow
     */
    fun getLimit(packageName: String): Flow<AppTimeLimit>

    /**
     * Get the specified app packageName time Limit synchronously
     */
    suspend fun getSync(packageName: String): AppTimeLimit

    suspend fun setTimeLimit(appTimeLimit: AppTimeLimit)
}
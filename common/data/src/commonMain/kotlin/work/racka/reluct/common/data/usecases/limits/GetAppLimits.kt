package work.racka.reluct.common.data.usecases.limits

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.limits.AppLimits

interface GetAppLimits {
    suspend fun getApp(packageName: String): Flow<AppLimits>
    suspend fun getAppSync(packageName: String): AppLimits
}
package work.racka.reluct.common.data.usecases.limits

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.app_info.AppInfo

interface ManageDistractingApps {
    /**
     * Returns a [Flow] of pair of two Lists typed [AppInfo]
     * The first value of the pair is a list of Distracting apps
     * The second value of the pair is a list of Non Distracting Apps present in the user device
     */
    suspend fun invoke(): Flow<Pair<List<AppInfo>, List<AppInfo>>>

    suspend fun markAsDistracting(packageName: String)

    suspend fun markAsNotDistracting(packageName: String)
}

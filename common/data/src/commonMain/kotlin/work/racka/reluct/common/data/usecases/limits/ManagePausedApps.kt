package work.racka.reluct.common.data.usecases.limits

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.app_info.AppInfo

interface ManagePausedApps {
    /**
     * Returns a [Flow] of pair of two Lists typed [AppInfo]
     * The first value of the pair is a list of Paused apps
     * The second value of the pair is a list of Non Paused Apps present in the user device
     */
    suspend operator fun invoke(): Flow<Pair<List<AppInfo>, List<AppInfo>>>

    suspend fun pauseApp(packageName: String)
    suspend fun unPauseApp(packageName: String)
}
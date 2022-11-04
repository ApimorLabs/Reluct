package work.racka.reluct.common.features.screenTime.services

import kotlinx.coroutines.flow.Flow

interface ScreenTimeServices {
    suspend fun startLimitsService()
    fun stopLimitsService()
    fun observeCurrentAppBlocking(): Flow<BlockState>

    sealed class BlockState(val appPackageName: String) {
        class Blocked(appPackageName: String) : BlockState(appPackageName)
        class Allowed(appPackageName: String) : BlockState(appPackageName)
    }
}

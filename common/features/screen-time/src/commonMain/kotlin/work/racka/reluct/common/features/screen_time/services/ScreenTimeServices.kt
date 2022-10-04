package work.racka.reluct.common.features.screen_time.services

import kotlinx.coroutines.flow.Flow

interface ScreenTimeServices {
    fun startLimitsService()
    fun stopLimitsService()
    fun observeCurrentAppBlocking(): Flow<BlockState>

    sealed class BlockState(val appPackageName: String) {
        class Blocked(appPackageName: String) : BlockState(appPackageName)
        class Allowed(appPackageName: String) : BlockState(appPackageName)
    }
}
package work.racka.reluct.common.features.screenTime.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class DesktopScreenTimeServices : ScreenTimeServices {
    override suspend fun startLimitsService() { /** DESKTOP IMPL **/ }

    override fun stopLimitsService() { /** DESKTOP IMPL **/ }

    override fun observeCurrentAppBlocking(): Flow<ScreenTimeServices.BlockState> {
        return flowOf()
    }
}

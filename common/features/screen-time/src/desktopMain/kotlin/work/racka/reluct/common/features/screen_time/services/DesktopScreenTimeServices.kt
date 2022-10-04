package work.racka.reluct.common.features.screen_time.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class DesktopScreenTimeServices : ScreenTimeServices {
    override fun startLimitsService() {
    }

    override fun stopLimitsService() {
    }

    override fun observeCurrentAppBlocking(): Flow<ScreenTimeServices.BlockState> {
        return flowOf()
    }
}
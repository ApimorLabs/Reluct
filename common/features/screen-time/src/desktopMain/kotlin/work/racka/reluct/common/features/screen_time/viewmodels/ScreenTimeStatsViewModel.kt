package work.racka.reluct.common.features.screen_time.viewmodels

import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStats

actual class ScreenTimeStatsViewModel(scope: CoroutineScope) {

    actual val host: ScreenTimeStats by KoinJavaComponent.inject(ScreenTimeStats::class.java) {
        parametersOf(scope)
    }
}
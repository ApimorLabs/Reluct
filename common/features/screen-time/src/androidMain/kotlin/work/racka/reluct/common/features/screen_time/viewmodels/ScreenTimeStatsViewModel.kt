package work.racka.reluct.common.features.screen_time.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStats

actual class ScreenTimeStatsViewModel : ViewModel() {

    actual val host: ScreenTimeStats by inject(ScreenTimeStats::class.java) {
        parametersOf(viewModelScope)
    }
}
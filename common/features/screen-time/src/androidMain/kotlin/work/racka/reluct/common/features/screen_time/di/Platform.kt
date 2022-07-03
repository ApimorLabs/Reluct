package work.racka.reluct.common.features.screen_time.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.features.screen_time.viewmodels.ScreenTimeStatsViewModel

internal actual object Platform {
    actual fun installModule(): Module = module {
        viewModel {
            ScreenTimeStatsViewModel()
        }
    }
}
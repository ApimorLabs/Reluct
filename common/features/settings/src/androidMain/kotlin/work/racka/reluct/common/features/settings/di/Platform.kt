package work.racka.reluct.common.features.settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import work.racka.reluct.common.features.settings.viewmodels.AppSettingsViewModel

internal actual object Platform {
    actual fun module() = module {
        viewModel {
            AppSettingsViewModel(settings = get())
        }
    }
}
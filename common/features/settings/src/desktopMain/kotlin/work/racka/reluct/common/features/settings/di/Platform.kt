package work.racka.reluct.common.features.settings.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.features.settings.viewmodels.AppSettingsViewModel

internal actual object Platform {
    actual fun module(): Module = module {
        factory {
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            AppSettingsViewModel(
                settings = get(),
                scope = viewModelScope
            )
        }
    }
}
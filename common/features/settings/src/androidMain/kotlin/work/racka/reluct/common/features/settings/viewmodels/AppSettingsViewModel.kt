package work.racka.reluct.common.features.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import work.racka.reluct.common.features.settings.AppSettings
import work.racka.reluct.common.features.settings.AppSettingsImpl
import work.racka.reluct.common.settings.MultiplatformSettings

actual class AppSettingsViewModel(
    settings: MultiplatformSettings
) : ViewModel() {
    actual val host: AppSettings by lazy {
        AppSettingsImpl(
            settings = settings,
            scope = viewModelScope
        )
    }
}
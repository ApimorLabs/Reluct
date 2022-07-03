package work.racka.reluct.common.features.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.settings.AppSettings

actual class AppSettingsViewModel : ViewModel() {
    actual val host: AppSettings by inject(AppSettings::class.java) {
        parametersOf(viewModelScope)
    }
}
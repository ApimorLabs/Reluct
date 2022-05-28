package work.racka.reluct.common.features.settings.viewmodels

import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.settings.AppSettings
import work.racka.reluct.common.features.settings.AppSettingsImpl
import work.racka.reluct.common.settings.MultiplatformSettings

actual class AppSettingsViewModel(
    settings: MultiplatformSettings,
    scope: CoroutineScope
) {
    actual val host: AppSettings by lazy {
        AppSettingsImpl(
            settings = settings,
            scope = scope
        )
    }
}
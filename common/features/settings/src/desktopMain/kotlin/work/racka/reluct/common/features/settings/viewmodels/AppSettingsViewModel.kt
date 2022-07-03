package work.racka.reluct.common.features.settings.viewmodels

import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import work.racka.reluct.common.features.settings.AppSettings

actual class AppSettingsViewModel(scope: CoroutineScope) {
    actual val host: AppSettings by KoinJavaComponent.inject(AppSettings::class.java) {
        parametersOf(scope)
    }
}
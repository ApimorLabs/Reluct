package work.racka.reluct.common.features.settings.di

import kotlinx.coroutines.CoroutineScope
import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.reluct.common.features.settings.AppSettings
import work.racka.reluct.common.features.settings.AppSettingsImpl

object AppSettings {
    fun KoinApplication.appSettingsModules() {
        modules(Platform.module(), commonModule())
    }

    private fun commonModule() = module {
        factory<AppSettings> { (scope: CoroutineScope) ->
            AppSettingsImpl(settings = get(), scope = scope)
        }
    }
}
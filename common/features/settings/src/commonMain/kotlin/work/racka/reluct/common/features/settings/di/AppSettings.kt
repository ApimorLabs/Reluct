package work.racka.reluct.common.features.settings.di

import org.koin.core.KoinApplication

object AppSettings {
    fun KoinApplication.appSettingsModules() {
        modules(Platform.module())
    }
}
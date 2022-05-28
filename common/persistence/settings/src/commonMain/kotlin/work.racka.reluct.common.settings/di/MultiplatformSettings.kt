package work.racka.reluct.common.settings.di

import com.russhwolf.settings.Settings
import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.reluct.common.settings.MultiplatformSettings
import work.racka.reluct.common.settings.MultiplatformSettingsImpl

object MultiplatformSettings {

    fun KoinApplication.settingsModules() =
        this.apply {
            modules(
                commonModule(),
                Platform.platformSettingsModule()
            )
        }

    private fun commonModule() = module {
        single<MultiplatformSettings> {
            val settings = Settings()
            MultiplatformSettingsImpl(settings)
        }
    }
}
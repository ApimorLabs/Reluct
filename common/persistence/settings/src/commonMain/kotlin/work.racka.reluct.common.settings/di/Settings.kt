package work.racka.thinkrchive.v2.common.settings.di

import com.russhwolf.settings.Settings
import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.thinkrchive.v2.common.settings.repository.SettingsRepository
import work.racka.thinkrchive.v2.common.settings.repository.SettingsRepositoryImpl

object Settings {

    fun KoinApplication.settingsModules() =
        this.apply {
            modules(
                commonModule(),
                Platform.platformSettingsModule()
            )
        }

    private fun commonModule() = module {
        single<SettingsRepository> {
            val settings = Settings()
            SettingsRepositoryImpl(settings)
        }
    }
}
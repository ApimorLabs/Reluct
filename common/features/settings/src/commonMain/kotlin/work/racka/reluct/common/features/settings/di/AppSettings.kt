package work.racka.reluct.common.features.settings.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.features.settings.AppSettingsViewModel
import work.racka.reluct.common.features.settings.UserAccountViewModel

object AppSettings {
    fun KoinApplication.appSettingsModules() {
        modules(commonModule())
    }

    private fun commonModule() = module {
        commonViewModel {
            AppSettingsViewModel(
                settings = get(),
                screenTimeServices = get(),
                manageCoffeeProducts = get()
            )
        }

        commonViewModel {
            UserAccountViewModel(
                manageUser = get()
            )
        }
    }
}

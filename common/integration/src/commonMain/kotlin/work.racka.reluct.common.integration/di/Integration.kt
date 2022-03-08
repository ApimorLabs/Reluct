package work.racka.reluct.common.integration.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.reluct.common.integration.containers.settings.AppSettings


internal object Integration {

    fun KoinApplication.integrationModules() =
        this.apply {
            modules(
                commonModule(),
                work.racka.reluct.common.integration.di.Platform.platformIntegrationModule()
            )
        }

    private fun commonModule() = module {
        single {
            CoroutineScope(Dispatchers.Default + SupervisorJob())
        }

        single {
            AppSettings(
                settings = get(),
                scope = get()
            )
        }
    }
}
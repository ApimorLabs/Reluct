package work.racka.reluct.common.integration.di

import org.koin.core.KoinApplication
import org.koin.dsl.module


internal object Integration {

    fun KoinApplication.integrationModules() =
        this.apply {
            modules(
                commonModule(),
                Platform.platformIntegrationModule()
            )
        }

    private fun commonModule() = module {

    }
}
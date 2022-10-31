package work.racka.reluct.common.services.di

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

object SystemServices {
    fun KoinApplication.install() = apply {
        modules(
            commonModule(),
            platformSystemServicesModule()
        )
    }

    private fun commonModule() = module { }
}

internal expect fun platformSystemServicesModule(): Module

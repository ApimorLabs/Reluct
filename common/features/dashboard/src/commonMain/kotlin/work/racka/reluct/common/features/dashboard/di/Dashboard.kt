package work.racka.reluct.common.features.dashboard.di

import org.koin.core.KoinApplication
import org.koin.dsl.module

object Dashboard {
    fun KoinApplication.install() = apply {
        modules(commonModule())
    }

    private fun commonModule() = module {

    }
}
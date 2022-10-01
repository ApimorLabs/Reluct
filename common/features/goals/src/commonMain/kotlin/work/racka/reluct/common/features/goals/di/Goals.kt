package work.racka.reluct.common.features.goals.di

import org.koin.core.KoinApplication
import org.koin.dsl.module

object Goals {
    fun KoinApplication.install() = apply {
        modules(commonModule())
    }

    private fun commonModule() = module {

    }
}
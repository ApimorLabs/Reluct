package work.racka.reluct.common.features.onboarding.di

import org.koin.core.KoinApplication
import org.koin.dsl.module

object Onboarding {
    fun KoinApplication.install() = apply { modules(commonModule()) }

    private fun commonModule() = module { }
}
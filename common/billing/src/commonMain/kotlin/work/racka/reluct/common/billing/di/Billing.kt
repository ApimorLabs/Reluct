package work.racka.reluct.common.billing.di

import org.koin.core.KoinApplication
import org.koin.dsl.module

object Billing {

    fun KoinApplication.install() = apply {
        modules(Platform.module(), commonModule())
    }

    private fun commonModule() = module { }
}
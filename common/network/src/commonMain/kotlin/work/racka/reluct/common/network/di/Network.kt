package work.racka.reluct.common.network.di

import org.koin.core.KoinApplication
import org.koin.dsl.module

object Network {
    fun KoinApplication.install() = apply {
        modules(commonModule(), Platform.module())
    }

    private fun commonModule() = module { }
}

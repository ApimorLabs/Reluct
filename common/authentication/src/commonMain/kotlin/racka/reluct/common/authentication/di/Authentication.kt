package racka.reluct.common.authentication.di

import org.koin.core.KoinApplication
import org.koin.dsl.module

object Authentication {
    fun KoinApplication.installModules() = apply {
        modules(commonModule(), Platform.installModule())
    }

    private fun commonModule() = module { }
}

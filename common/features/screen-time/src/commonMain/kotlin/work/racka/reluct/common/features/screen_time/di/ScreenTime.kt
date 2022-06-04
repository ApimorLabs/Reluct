package work.racka.reluct.common.features.screen_time.di

import org.koin.core.KoinApplication
import org.koin.dsl.module

object ScreenTime {
    fun KoinApplication.screenTimeModules() = this.apply {
        modules(commonModule(), Platform.installModule())
    }

    private fun commonModule() = module {

    }
}
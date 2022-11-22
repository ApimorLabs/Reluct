package work.racka.reluct.common.app.usage.stats.di

import org.koin.core.KoinApplication
import org.koin.dsl.module

object AppUsageStats {

    fun KoinApplication.appUsageModules() =
        this.apply {
            modules(
                commonModule(),
                Platform.module()
            )
        }

    private fun commonModule() = module {
    }
}

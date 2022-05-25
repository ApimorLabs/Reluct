package work.racka.reluct.common.app.usage.stats.di

import org.koin.core.KoinApplication
import org.koin.dsl.module

object Data {

    fun KoinApplication.dataModules() =
        this.apply {
            modules(
                commonModule(),
                Platform.platformTasksModule()
            )
        }

    private fun commonModule() = module {

    }
}
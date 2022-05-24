package work.racka.reluct.common.features.tasks.di

import org.koin.core.KoinApplication
import org.koin.dsl.module

object Tasks {

    fun KoinApplication.tasksModules() =
        this.apply {
            modules(
                commonModule(),
                Platform.platformTasksModule()
            )
        }

    private fun commonModule() = module {}
}
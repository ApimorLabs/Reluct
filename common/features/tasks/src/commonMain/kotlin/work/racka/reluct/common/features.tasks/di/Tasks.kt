package work.racka.reluct.common.features.tasks.di

import org.koin.core.KoinApplication

object Tasks {

    fun KoinApplication.tasksModules() {
        modules(Platform.platformTasksModule())
    }

    //private fun commonModule() = module {}
}
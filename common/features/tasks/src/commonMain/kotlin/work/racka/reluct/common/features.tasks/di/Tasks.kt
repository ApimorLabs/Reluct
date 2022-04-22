package work.racka.reluct.common.features.tasks.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.usecases.impl.GetTasksUseCaseImpl
import work.racka.reluct.common.features.tasks.usecases.impl.ModifyTasksUseCaseImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTasksUseCase

object Tasks {

    fun KoinApplication.tasksModules() =
        this.apply {
            modules(
                commonModule(),
                Platform.platformTasksModule()
            )
        }

    private fun commonModule() = module {

        factory<GetTasksUseCase> {
            GetTasksUseCaseImpl(
                dao = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<ModifyTasksUseCase> {
            ModifyTasksUseCaseImpl(
                dao = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }
    }
}
package work.racka.reluct.common.features.tasks.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.usecases.impl.*
import work.racka.reluct.common.features.tasks.usecases.interfaces.*

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

        factory<ModifyTaskUseCase> {
            ModifyTaskUseCaseImpl(
                dao = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<GetWeeklyTasksUseCase> {
            GetWeeklyTasksUseCaseImpl(
                dao = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<GetDailyTasksUseCase> {
            GetDailyTasksUseCaseImpl(
                weeklyTasks = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<GetWeekRangeFromOffset> { GetWeekRangeFromOffsetImpl() }
    }
}
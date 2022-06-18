package work.racka.reluct.common.database.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.reluct.common.database.dao.screentime.LimitsDao
import work.racka.reluct.common.database.dao.screentime.LimitsDaoImpl
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.database.dao.tasks.TasksDaoImpl

object Database {

    fun KoinApplication.databaseModules() =
        this.apply {
            modules(
                commonModule(),
                Platform.platformDatabaseModule()
            )
        }

    private fun commonModule() = module {
        factory<TasksDao> {
            TasksDaoImpl(
                dispatcher = CoroutineDispatchers.backgroundDispatcher,
                databaseWrapper = get()
            )
        }

        factory<LimitsDao> {
            LimitsDaoImpl(
                dispatcher = CoroutineDispatchers.backgroundDispatcher,
                databaseWrapper = get()
            )
        }
    }
}
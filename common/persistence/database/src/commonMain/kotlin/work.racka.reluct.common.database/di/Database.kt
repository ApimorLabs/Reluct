package work.racka.reluct.common.database.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinApplication
import org.koin.dsl.module
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
                coroutineScope = CoroutineScope(CoroutineDispatchers.backgroundDispatcher + SupervisorJob()),
                databaseWrapper = get()
            )
        }
    }
}
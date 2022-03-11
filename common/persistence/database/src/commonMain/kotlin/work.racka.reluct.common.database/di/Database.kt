package work.racka.reluct.common.database.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        single {
            CoroutineScope(Dispatchers.Default + SupervisorJob())
        }

        single<TasksDao> {
            TasksDaoImpl(
                coroutineScope = get(),
                databaseWrapper = get()
            )
        }
    }
}
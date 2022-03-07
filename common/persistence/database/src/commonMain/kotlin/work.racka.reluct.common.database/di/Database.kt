package work.racka.thinkrchive.v2.common.database.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.thinkrchive.v2.common.database.dao.ThinkpadDao
import work.racka.thinkrchive.v2.common.database.dao.ThinkpadDaoImpl

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

        single<ThinkpadDao> {
            ThinkpadDaoImpl(
                coroutineScope = get(),
                thinkpadDatabaseWrapper = get()
            )
        }
    }
}
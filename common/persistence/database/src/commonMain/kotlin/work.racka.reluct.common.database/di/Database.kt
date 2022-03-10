package work.racka.thinkrchive.v2.common.database.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinApplication
import org.koin.dsl.module

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

        single {

        }
    }
}
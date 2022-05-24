package work.racka.reluct.common.integration.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import work.racka.reluct.common.data.di.Data
import work.racka.reluct.common.database.di.Database
import work.racka.reluct.common.features.tasks.di.Tasks
import work.racka.reluct.common.settings.di.MultiplatformSettings

object KoinMain {
    // This should be used in every target as a starting point for Koin
    fun initKoin(
        enableNetworkLogs: Boolean = false,
        appDeclaration: KoinAppDeclaration = {}
    ) = startKoin {
        appDeclaration()

        // Modules in common directory
        with(Database) {
            databaseModules()
        }
        with(MultiplatformSettings) {
            settingsModules()
        }
        with(Integration) {
            integrationModules()
        }
        with(Tasks) {
            tasksModules()
        }

        with(Data) { dataModules() }
    }
}
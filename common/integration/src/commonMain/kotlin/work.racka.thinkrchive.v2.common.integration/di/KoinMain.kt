package work.racka.thinkrchive.v2.common.integration.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

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
        with(Settings) {
            settingsModules()
        }
        with(Integration) {
            integrationModules()
        }
    }
}
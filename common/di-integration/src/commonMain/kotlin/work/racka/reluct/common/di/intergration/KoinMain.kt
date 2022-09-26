package work.racka.reluct.common.di.intergration

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import racka.reluct.common.authentication.di.Authentication
import work.racka.reluct.common.app.usage.stats.di.AppUsageStats
import work.racka.reluct.common.data.di.Data
import work.racka.reluct.common.database.di.Database
import work.racka.reluct.common.features.dashboard.di.Dashboard
import work.racka.reluct.common.features.onboarding.di.OnBoarding
import work.racka.reluct.common.features.screen_time.di.ScreenTime
import work.racka.reluct.common.features.settings.di.AppSettings
import work.racka.reluct.common.features.tasks.di.Tasks
import work.racka.reluct.common.settings.di.MultiplatformSettings
import work.racka.reluct.common.system_service.di.SystemServices

object KoinMain {
    // This should be used in every platform target as a starting point for Koin
    fun initKoin(
        //enableNetworkLogs: Boolean = false,
        appDeclaration: KoinAppDeclaration = {}
    ) = startKoin {
        appDeclaration()

        /** Modules in common directory **/
        // Data Sources
        Authentication.run { installModules() }
        Database.run { databaseModules() }
        MultiplatformSettings.run { settingsModules() }
        AppUsageStats.run { appUsageModules() }

        // Data Layer & Models
        Data.run { dataModules() }

        // Features
        Dashboard.run { install() }
        Tasks.run { tasksModules() }
        ScreenTime.run { screenTimeModules() }
        AppSettings.run { appSettingsModules() }
        OnBoarding.run { install() }

        // Other Services
        SystemServices.run { install() }
    }
}
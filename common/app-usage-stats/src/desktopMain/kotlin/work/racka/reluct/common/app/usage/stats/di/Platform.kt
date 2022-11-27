package work.racka.reluct.common.app.usage.stats.di

import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.app.usage.stats.manager.DesktopUsageDataManager
import work.racka.reluct.common.app.usage.stats.manager.UsageDataManager

internal actual object Platform {
    actual fun module(): Module = module {
        single<UsageDataManager> { DesktopUsageDataManager() }
    }
}

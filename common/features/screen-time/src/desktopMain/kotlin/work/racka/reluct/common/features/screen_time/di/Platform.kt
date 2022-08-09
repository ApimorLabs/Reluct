package work.racka.reluct.common.features.screen_time.di

import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.features.screen_time.services.DesktopScreenTimeServices
import work.racka.reluct.common.features.screen_time.services.ScreenTimeServices

internal actual object Platform {
    actual fun installModule(): Module = module {
        factory<ScreenTimeServices> {
            DesktopScreenTimeServices()
        }
    }
}
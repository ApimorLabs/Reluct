package work.racka.reluct.common.features.screenTime.di

import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.features.screenTime.services.DesktopScreenTimeServices
import work.racka.reluct.common.features.screenTime.services.ScreenTimeServices

internal actual object Platform {
    actual fun installModule(): Module = module {
        factory<ScreenTimeServices> {
            DesktopScreenTimeServices()
        }
    }
}

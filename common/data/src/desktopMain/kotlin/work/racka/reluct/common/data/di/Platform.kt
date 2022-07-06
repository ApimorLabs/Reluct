package work.racka.reluct.common.data.di

import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.data.usecases.app_info.DesktopGetAppInfo
import work.racka.reluct.common.data.usecases.app_info.DesktopGetInstalledApps
import work.racka.reluct.common.data.usecases.app_info.GetAppInfo
import work.racka.reluct.common.data.usecases.app_info.GetInstalledApps

internal actual object Platform {
    actual fun installModule(): Module = module {
        factory<GetAppInfo> {
            DesktopGetAppInfo()
        }

        factory<GetInstalledApps> { DesktopGetInstalledApps() }
    }
}

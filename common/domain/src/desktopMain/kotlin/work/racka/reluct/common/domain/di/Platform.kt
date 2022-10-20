package work.racka.reluct.common.domain.di

import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.domain.usecases.app_info.DesktopGetAppInfo
import work.racka.reluct.common.domain.usecases.app_info.DesktopGetInstalledApps
import work.racka.reluct.common.domain.usecases.app_info.GetAppInfo
import work.racka.reluct.common.domain.usecases.app_info.GetInstalledApps
import work.racka.reluct.common.domain.usecases.tasks.DesktopManageTasksAlarms
import work.racka.reluct.common.domain.usecases.tasks.ManageTasksAlarms

internal actual object Platform {
    actual fun installModule(): Module = module {
        factory<GetAppInfo> {
            DesktopGetAppInfo()
        }

        factory<GetInstalledApps> { DesktopGetInstalledApps() }

        factory<ManageTasksAlarms> { DesktopManageTasksAlarms() }
    }
}

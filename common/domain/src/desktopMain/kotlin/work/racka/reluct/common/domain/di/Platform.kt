package work.racka.reluct.common.domain.di

import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.domain.usecases.appInfo.DesktopGetAppInfo
import work.racka.reluct.common.domain.usecases.appInfo.DesktopGetInstalledApps
import work.racka.reluct.common.domain.usecases.appInfo.GetAppInfo
import work.racka.reluct.common.domain.usecases.appInfo.GetInstalledApps
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

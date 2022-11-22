package work.racka.reluct.common.domain.di

import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.domain.usecases.appInfo.AndroidGetAppInfo
import work.racka.reluct.common.domain.usecases.appInfo.AndroidGetInstalledApps
import work.racka.reluct.common.domain.usecases.appInfo.GetAppInfo
import work.racka.reluct.common.domain.usecases.appInfo.GetInstalledApps
import work.racka.reluct.common.domain.usecases.tasks.AndroidManageTasksAlarms
import work.racka.reluct.common.domain.usecases.tasks.ManageTasksAlarms

internal actual object Platform {
    actual fun installModule(): Module = module {
        factory<GetAppInfo> {
            AndroidGetAppInfo(context = androidContext())
        }

        factory<GetInstalledApps> {
            AndroidGetInstalledApps(
                context = androidContext(),
                getAppInfo = get(),
                dispatcher = Dispatchers.IO
            )
        }

        factory<ManageTasksAlarms> {
            AndroidManageTasksAlarms(
                context = androidContext(),
                getTasks = get(),
                dispatcher = Dispatchers.IO
            )
        }
    }
}

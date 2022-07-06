package work.racka.reluct.common.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.data.usecases.app_info.AndroidGetAppInfo
import work.racka.reluct.common.data.usecases.app_info.AndroidGetInstalledApps
import work.racka.reluct.common.data.usecases.app_info.GetAppInfo
import work.racka.reluct.common.data.usecases.app_info.GetInstalledApps

internal actual object Platform {
    actual fun installModule(): Module = module {
        factory<GetAppInfo> {
            AndroidGetAppInfo(context = androidContext())
        }

        factory<GetInstalledApps> {
            AndroidGetInstalledApps(context = androidContext(), getAppInfo = get())
        }
    }
}

package work.racka.reluct.common.data.di

import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.data.usecases.app_info.AndroidGetAppInfo
import work.racka.reluct.common.data.usecases.app_info.GetAppInfo

internal actual object Platform {
    actual fun installModule(): Module = module {
        factory<GetAppInfo> {
            AndroidGetAppInfo(
                context = androidApplication().applicationContext
            )
        }
    }
}

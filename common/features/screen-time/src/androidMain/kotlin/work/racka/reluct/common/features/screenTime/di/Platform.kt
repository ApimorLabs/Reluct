package work.racka.reluct.common.features.screenTime.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.features.screenTime.services.AndroidScreenTimeServices
import work.racka.reluct.common.features.screenTime.services.ScreenTimeServices

internal actual object Platform {
    actual fun installModule(): Module = module {
        factory<ScreenTimeServices> {
            AndroidScreenTimeServices(
                context = androidContext(),
                getAppUsageInfo = get(),
                getAppLimits = get(),
                manageFocusMode = get(),
            )
        }
    }
}

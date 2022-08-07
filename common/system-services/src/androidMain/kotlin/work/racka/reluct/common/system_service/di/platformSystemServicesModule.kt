package work.racka.reluct.common.system_service.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.system_service.haptics.HapticFeedback

internal actual fun platformSystemServicesModule(): Module = module {
    factory {
        HapticFeedback(context = androidContext())
    }
}
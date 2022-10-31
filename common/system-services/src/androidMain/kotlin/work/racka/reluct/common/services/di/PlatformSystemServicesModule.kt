package work.racka.reluct.common.services.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.services.haptics.AndroidHapticFeedback
import work.racka.reluct.common.services.haptics.HapticFeedback

internal actual fun platformSystemServicesModule(): Module = module {
    single<HapticFeedback> {
        AndroidHapticFeedback(context = androidContext())
    }
}

package work.racka.reluct.common.services.di

import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.services.haptics.DesktopHapticFeedback
import work.racka.reluct.common.services.haptics.HapticFeedback

internal actual fun platformSystemServicesModule(): Module = module {
    single<HapticFeedback> {
        DesktopHapticFeedback()
    }
}

package work.racka.reluct.common.features.onboarding.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.features.onboarding.vm.OnBoardingViewModel

object OnBoarding {
    fun KoinApplication.install() = apply { modules(commonModule()) }

    private fun commonModule() = module {
        commonViewModel {
            OnBoardingViewModel(
                settings = get()
            )
        }
    }
}

package work.racka.reluct.common.features.goals.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.features.goals.active.ActiveGoalsViewModel

object GoalsDI {
    fun KoinApplication.install() = apply {
        modules(commonModule())
    }

    private fun commonModule() = module {
        commonViewModel {
            ActiveGoalsViewModel(
                getGoals = get(),
                modifyGoals = get()
            )
        }
    }
}
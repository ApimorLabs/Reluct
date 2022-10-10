package work.racka.reluct.common.features.goals.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.features.goals.active.ActiveGoalsViewModel
import work.racka.reluct.common.features.goals.add_edit_goal.AddEditGoalViewModel

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

        commonViewModel { (goalId: String?, defaultGoalIndex: Int?) ->
            AddEditGoalViewModel(
                getGoals = get(),
                modifyGoals = get(),
                goalId = goalId,
                defaultGoalIndex = defaultGoalIndex
            )
        }
    }
}
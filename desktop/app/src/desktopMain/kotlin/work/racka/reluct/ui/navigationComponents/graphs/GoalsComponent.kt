package work.racka.reluct.ui.navigationComponents.graphs

import com.arkivanov.decompose.router.stack.ChildStack
import work.racka.reluct.common.core.navigation.destination.graphs.GoalsConfig
import work.racka.reluct.common.core.navigation.destination.graphs.GoalsExtrasConfig
import work.racka.reluct.ui.screens.ComposeRenderer
import work.racka.reluct.ui.screens.goals.active.ActiveGoalsComponent
import work.racka.reluct.ui.screens.goals.addEdit.AddEditGoalComponent
import work.racka.reluct.ui.screens.goals.details.GoalDetailsComponent
import work.racka.reluct.ui.screens.goals.inactive.InactiveGoalsComponent

interface GoalsComponent : ComposeRenderer {

    val mainChildStack: ChildStack<GoalsConfig, MainChild>
    val itemsChildStack: ChildStack<GoalsExtrasConfig, ItemsChild>

    fun openGoalDetails(goalId: String?)
    fun addEditGoal(goalId: String?, defaultGoalIndex: Int?)

    sealed class MainChild {
        class ActiveGoal(val component: ActiveGoalsComponent) : MainChild()
        class InactiveGoal(val component: InactiveGoalsComponent) : MainChild()
    }

    sealed class ItemsChild {
        class AddEdit(val component: AddEditGoalComponent) : ItemsChild()
        class Details(val component: GoalDetailsComponent) : ItemsChild()
        object None : ItemsChild()
    }
}

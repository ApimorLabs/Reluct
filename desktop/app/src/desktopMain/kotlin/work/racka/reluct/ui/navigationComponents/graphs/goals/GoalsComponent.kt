package work.racka.reluct.ui.navigationComponents.graphs.goals

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.destination.graphs.GoalsConfig
import work.racka.reluct.common.core.navigation.destination.graphs.GoalsExtrasConfig
import work.racka.reluct.ui.screens.ComposeRenderer
import work.racka.reluct.ui.screens.goals.active.ActiveGoalsComponent
import work.racka.reluct.ui.screens.goals.addEdit.AddEditGoalComponent
import work.racka.reluct.ui.screens.goals.details.GoalDetailsComponent
import work.racka.reluct.ui.screens.goals.inactive.InactiveGoalsComponent

interface GoalsComponent : ComposeRenderer {

    val mainChildStack: Value<ChildStack<GoalsConfig, MainChild>>
    val itemsChildStack: Value<ChildStack<GoalsExtrasConfig, ItemsChild>>

    sealed class MainChild {
        data class ActiveGoal(val component: ActiveGoalsComponent) : MainChild()
        data class InactiveGoal(val component: InactiveGoalsComponent) : MainChild()
    }

    sealed class ItemsChild {
        data class AddEdit(val component: AddEditGoalComponent) : ItemsChild()
        data class Details(val component: GoalDetailsComponent) : ItemsChild()
        object None : ItemsChild()
    }
}

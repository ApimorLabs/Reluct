package work.racka.reluct.ui.navigationComponents.graphs.goals

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.destination.graphs.GoalsConfig
import work.racka.reluct.ui.screens.goals.active.ActiveGoalsComponent
import work.racka.reluct.ui.screens.goals.inactive.InactiveGoalsComponent

internal class MainGoalsComponentRouter(
    componentContext: ComponentContext,
    initialStack: List<GoalsConfig>,
    private val onAddGoal: (defaultGoalIndex: Int?) -> Unit,
    private val onShowDetails: (goalId: String) -> Unit,
    private val onExit: () -> Unit
) {

    private val navigation = StackNavigation<GoalsConfig>()

    val stack: Value<ChildStack<GoalsConfig, GoalsComponent.MainChild>> =
        componentContext.childStack(
            source = navigation,
            initialStack = { initialStack.ifEmpty { listOf(GoalsConfig.Active) } },
            childFactory = ::createChild,
        )

    private fun createChild(config: GoalsConfig, context: ComponentContext) = when (config) {
        is GoalsConfig.Active -> GoalsComponent.MainChild.ActiveGoal(createActiveGoals(context))
        is GoalsConfig.Inactive -> GoalsComponent.MainChild.InactiveGoal(createInactiveGoals(context))
    }

    private fun createActiveGoals(componentContext: ComponentContext) = ActiveGoalsComponent(
        componentContext = componentContext,
        onAddGoal = onAddGoal,
        onShowDetails = onShowDetails,
        onExit = onExit
    )

    private fun createInactiveGoals(componentContext: ComponentContext) = InactiveGoalsComponent(
        componentContext = componentContext,
        onAddGoal = onAddGoal,
        onShowDetails = onShowDetails,
        onGoBack = { navigation.pop { isSuccess -> if (!isSuccess) onExit() } }
    )

    fun openActiveGoals() = navigation.bringToFront(GoalsConfig.Active)
    fun openInactiveGoals() = navigation.bringToFront(GoalsConfig.Inactive)
}

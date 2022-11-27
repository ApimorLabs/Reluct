package work.racka.reluct.ui.navigationComponents.graphs.goals

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.destination.graphs.GoalsConfig
import work.racka.reluct.common.core.navigation.destination.graphs.GoalsExtrasConfig

class DefaultGoalsComponent(
    componentContext: ComponentContext,
    initialMainStack: () -> List<GoalsConfig>,
    initialItemsStack: () -> List<GoalsExtrasConfig>,
) : GoalsComponent, ComponentContext by componentContext {

    private val itemsChildRouter = GoalItemsComponentRouter(
        componentContext = componentContext,
        initialStack = initialItemsStack()
    )

    private val mainChildRouter = MainGoalsComponentRouter(
        componentContext = this,
        initialStack = initialMainStack(),
        onShowDetails = itemsChildRouter::openDetails,
        onAddGoal = { itemsChildRouter.openAddEdit(defaultGoalIndex = it, goalId = null) },
    )

    override val mainChildStack: Value<ChildStack<GoalsConfig, GoalsComponent.MainChild>> =
        mainChildRouter.stack

    override val itemsChildStack: Value<ChildStack<GoalsExtrasConfig, GoalsComponent.ItemsChild>> =
        itemsChildRouter.stack

    // Render the combined components here.
    @Composable
    override fun Render(modifier: Modifier) {
        GoalsRootChildren(
            mainChildStack = mainChildStack,
            itemsChildStack = itemsChildStack,
            onUpdateConfig = { config ->
                when (config) {
                    is GoalsConfig.Active -> mainChildRouter.openActiveGoals()
                    is GoalsConfig.Inactive -> mainChildRouter.openInactiveGoals()
                }
            }
        )
    }
}

package work.racka.reluct.ui.navigationComponents.graphs.goals

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.destination.graphs.GoalsExtrasConfig
import work.racka.reluct.ui.screens.goals.addEdit.AddEditGoalComponent
import work.racka.reluct.ui.screens.goals.details.GoalDetailsComponent

class GoalItemsComponentRouter(
    componentContext: ComponentContext,
    initialStack: List<GoalsExtrasConfig>
) {

    private val navigation = StackNavigation<GoalsExtrasConfig>()

    val stack: Value<ChildStack<GoalsExtrasConfig, GoalsComponent.ItemsChild>> =
        componentContext.childStack(
            source = navigation,
            initialStack = { (listOf(GoalsExtrasConfig.None) + initialStack).distinct() },
            childFactory = ::createChild,
            key = "GoalsItemsChild"
        )

    private fun createChild(config: GoalsExtrasConfig, context: ComponentContext) = when (config) {
        is GoalsExtrasConfig.AddEdit -> GoalsComponent.ItemsChild.AddEdit(
            createAddEdit(
                componentContext = context,
                goalId = config.goalId,
                defaultGoalIndex = config.defaultGoalIndex
            )
        )
        is GoalsExtrasConfig.Details -> GoalsComponent.ItemsChild.Details(
            createDetails(
                componentContext = context,
                goalId = config.goalId
            )
        )
        is GoalsExtrasConfig.None -> GoalsComponent.ItemsChild.None
    }

    private fun createAddEdit(
        componentContext: ComponentContext,
        goalId: String?,
        defaultGoalIndex: Int?
    ) = AddEditGoalComponent(
        componentContext = componentContext,
        onExit = navigation::pop,
        defaultGoalIndex = defaultGoalIndex,
        goalId = goalId
    )

    private fun createDetails(componentContext: ComponentContext, goalId: String?) =
        GoalDetailsComponent(
            componentContext = componentContext,
            goalId = goalId,
            onEdit = { openAddEdit(goalId = it, defaultGoalIndex = null) },
            onExit = navigation::pop
        )

    fun openAddEdit(goalId: String?, defaultGoalIndex: Int?) = navigation.navigate { stack ->
        stack.dropLastWhile { it is GoalsExtrasConfig.AddEdit }
            .plus(GoalsExtrasConfig.AddEdit(goalId = goalId, defaultGoalIndex = defaultGoalIndex))
    }

    fun openDetails(goalId: String?) = navigation.navigate { stack ->
        stack.dropLastWhile { it is GoalsExtrasConfig.Details }
            .plus(GoalsExtrasConfig.Details(goalId = goalId))
    }
}

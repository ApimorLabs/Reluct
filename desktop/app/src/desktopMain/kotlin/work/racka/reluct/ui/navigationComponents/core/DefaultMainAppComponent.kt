package work.racka.reluct.ui.navigationComponents.core

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.checks.InitialNavCheck
import work.racka.reluct.common.core.navigation.destination.AppNavConfig
import work.racka.reluct.common.core.navigation.destination.graphs.*
import work.racka.reluct.ui.navigationComponents.core.MainAppComponent.Child
import work.racka.reluct.ui.navigationComponents.graphs.goals.DefaultGoalsComponent
import work.racka.reluct.ui.navigationComponents.graphs.screenTime.DefaultScreenTimeComponent
import work.racka.reluct.ui.navigationComponents.graphs.tasks.DefaultTasksComponent
import work.racka.reluct.ui.screens.dashboard.DashboardComponent
import work.racka.reluct.ui.screens.onBoarding.OnBoardingComponent
import work.racka.reluct.ui.screens.settings.SettingsComponent

class DefaultMainAppComponent(
    componentContext: ComponentContext,
    private val initialCheck: Value<InitialNavCheck>
) : MainAppComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<AppNavConfig>()

    override val childStack: Value<ChildStack<AppNavConfig, Child>> = childStack(
        source = navigation,
        initialStack = { listOf(AppNavConfig.Checking) },
        childFactory = ::createChild,
        key = "AppRootStack"
    )

    private fun createChild(
        config: AppNavConfig,
        context: ComponentContext
    ): Child = when (config) {
        is AppNavConfig.Checking -> Child.Checking
        is AppNavConfig.OnBoarding -> Child.OnBoarding(OnBoardingComponent(context))
        is AppNavConfig.Dashboard -> Child.Dashboard(DashboardComponent(context))
        is AppNavConfig.Tasks -> Child.Tasks(createTasks(context, config))
        is AppNavConfig.ScreenTime -> Child.ScreenTime(createScreenTime(context, config))
        is AppNavConfig.Goals -> Child.Goals(createGoals(context, config))
        is AppNavConfig.Settings -> Child.Settings(SettingsComponent(context))
    }

    private fun createTasks(context: ComponentContext, config: AppNavConfig.Tasks) =
        DefaultTasksComponent(
            componentContext = context,
            initialMainStack = { config.initialTasksConfig },
            initialItemsStack = { config.initialTasksExtraConfig },
            onExit = ::goBack
        )

    private fun createGoals(context: ComponentContext, config: AppNavConfig.Goals) =
        DefaultGoalsComponent(
            componentContext = context,
            initialMainStack = { config.initialGoalsConfig },
            initialItemsStack = { config.initialGoalsExtrasConfig },
            onExit = ::goBack
        )

    private fun createScreenTime(context: ComponentContext, config: AppNavConfig.ScreenTime) =
        DefaultScreenTimeComponent(
            componentContext = context,
            initialItemsStack = { config.initialAppScreenTimeConfig },
            onExit = ::goBack
        )

    override fun openTasks(mainConfig: TasksConfig?, itemsConfig: TasksExtraConfig?) =
        navigation.bringToFront(
            AppNavConfig.Tasks(
                initialTasksConfig = mainConfig?.run { listOf(this) } ?: listOf(),
                initialTasksExtraConfig = itemsConfig?.run { listOf(this) } ?: listOf()
            )
        )

    override fun openScreenTime(itemsConfig: AppScreenTimeConfig?) = navigation.bringToFront(
        AppNavConfig.ScreenTime(
            initialAppScreenTimeConfig = itemsConfig?.run { listOf(this) } ?: listOf()
        )
    )

    override fun openGoals(mainConfig: GoalsConfig?, itemsConfig: GoalsExtrasConfig?) =
        navigation.bringToFront(
            AppNavConfig.Goals(
                initialGoalsConfig = mainConfig?.run { listOf(this) } ?: listOf(),
                initialGoalsExtrasConfig = itemsConfig?.run { listOf(this) } ?: listOf()
            )
        )

    override fun openDashboard() = navigation.navigate { stack ->
        stack.dropWhile { it !is AppNavConfig.Dashboard }
            .ifEmpty { listOf(AppNavConfig.Dashboard) }
    }

    override fun openSettings() = navigation.bringToFront(AppNavConfig.Settings)

    override fun goBack() = openDashboard()

    init {
        // Update Current Destination depending on the checks
        navCheck()
    }

    /**
     * Update Current Destination depending on the checks
     * It subscribes to [initialCheck] and update the nav stack as needed
     */
    private fun navCheck() {
        initialCheck.subscribe { check ->
            if (!check.isChecking) {
                if (check.isOnBoardingDone) {
                    navigation.navigate { stack ->
                        stack.drop(stack.size).plus(AppNavConfig.Dashboard)
                    }
                } else {
                    navigation.navigate { stack ->
                        stack.drop(stack.size).plus(AppNavConfig.OnBoarding)
                    }
                }
            }
        }
    }
}

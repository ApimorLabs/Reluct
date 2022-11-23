package work.racka.reluct.ui.navigationComponents.core

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.checks.InitialNavCheck
import work.racka.reluct.common.core.navigation.destination.AppNavConfig
import work.racka.reluct.common.core.navigation.destination.graphs.*
import work.racka.reluct.ui.navigationComponents.core.MainAppComponent.Child
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
        componentContext: ComponentContext
    ): Child = when (config) {
        is AppNavConfig.Checking -> Child.Checking
        is AppNavConfig.OnBoarding -> Child.OnBoarding(OnBoardingComponent(componentContext))
        is AppNavConfig.Dashboard -> Child.Dashboard(DashboardComponent(componentContext))
        is AppNavConfig.Tasks -> Child.Tasks()
        is AppNavConfig.ScreenTime -> Child.ScreenTime()
        is AppNavConfig.Goals -> Child.Goals()
        is AppNavConfig.Settings -> Child.Settings(SettingsComponent(componentContext))
    }

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

    override fun goBack() = navigation.pop()

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

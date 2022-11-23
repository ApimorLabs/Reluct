package work.racka.reluct.ui.navigationComponents

import com.arkivanov.decompose.router.stack.ChildStack
import work.racka.reluct.common.core.navigation.destination.AppNavConfig
import work.racka.reluct.common.core.navigation.destination.graphs.*
import work.racka.reluct.ui.navigationComponents.graphs.GoalsComponent
import work.racka.reluct.ui.navigationComponents.graphs.ScreenTimeComponent
import work.racka.reluct.ui.navigationComponents.graphs.TasksComponent
import work.racka.reluct.ui.screens.dashboard.DashboardComponent
import work.racka.reluct.ui.screens.onBoarding.OnBoardingComponent
import work.racka.reluct.ui.screens.settings.SettingsComponent

interface MainAppComponent {

    val childStack: ChildStack<AppNavConfig, Child>

    /**
     * Provide destinations if you try to launch a specific sub-destination of the Tasks Component
     * Passing [mainConfig] & [itemsConfig] as null means it will use the defaults of component
     */
    fun openTasks(mainConfig: TasksConfig? = null, itemsConfig: TasksExtraConfig? = null)

    /**
     * Provide destinations if you try to launch a specific sub-destination of the Tasks Component
     * Passing [itemsConfig] as null means it will use the defaults of component
     */
    fun openScreenTime(itemsConfig: AppScreenTimeConfig? = null)

    /**
     * Provide destinations if you try to launch a specific sub-destination of the Goals Component
     * Passing [mainConfig] & [itemsConfig] as null means it will use the defaults of component
     */
    fun openGoals(mainConfig: GoalsConfig? = null, itemsConfig: GoalsExtrasConfig? = null)

    /**
     * Open the Dashboard Component
     */
    fun openDashboard()

    /**
     * Open the Settings Component
     */
    fun openSettings()

    sealed class Child {
        class OnBoarding(val component: OnBoardingComponent) : Child()
        class Dashboard(val component: DashboardComponent) : Child()
        class Tasks(val component: TasksComponent) : Child()
        class ScreenTime(val component: ScreenTimeComponent) : Child()
        class Goals(val component: GoalsComponent) : Child()
        class Settings(val component: SettingsComponent) : Child()
    }
}

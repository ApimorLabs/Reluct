package work.racka.reluct.common.core.navigation.destination

import com.arkivanov.essenty.parcelable.Parcelable
import work.racka.reluct.common.core.navigation.destination.graphs.*

sealed class AppNavConfig : Parcelable {
    object Checking : AppNavConfig() // Should hide the nav rail
    object OnBoarding : AppNavConfig() // Should hide the nav rail
    object Dashboard : AppNavConfig()
    class Tasks(
        val initialTasksConfig: List<TasksConfig> = listOf(),
        val initialTasksExtraConfig: List<TasksExtraConfig> = listOf()
    ) : AppNavConfig() // Multipane - 2nd pane always active

    class ScreenTime(
        val initialAppScreenTimeConfig: List<AppScreenTimeConfig> = listOf()
    ) : AppNavConfig() // Multipane - adaptive

    class Goals(
        val initialGoalsConfig: List<GoalsConfig> = listOf(),
        val initialGoalsExtrasConfig: List<GoalsExtrasConfig> = listOf()
    ) : AppNavConfig() // Multipane - 2nd pane always active

    object Settings : AppNavConfig() // Should preferably be a dialog or a new Window
}

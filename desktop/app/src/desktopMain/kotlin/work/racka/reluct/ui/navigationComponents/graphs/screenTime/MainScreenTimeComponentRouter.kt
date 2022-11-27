package work.racka.reluct.ui.navigationComponents.graphs.screenTime

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.destination.graphs.ScreenTimeConfig
import work.racka.reluct.ui.screens.screenTime.statsAndLimits.ScreenTimeStatsLimitsComponent

class MainScreenTimeComponentRouter(
    componentContext: ComponentContext,
    private val isShowingAppStats: Value<Boolean>,
    private val onShowAppStats: (packageName: String?) -> Unit,
    private val onExit: () -> Unit
) {

    private val navigation = StackNavigation<ScreenTimeConfig>()

    val stack: Value<ChildStack<ScreenTimeConfig, ScreenTimeComponent.MainChild>> = componentContext
        .childStack(
            source = navigation,
            initialStack = { listOf(ScreenTimeConfig.StatsAndLimits) },
            childFactory = ::createChild,
            key = "ScreenTimeMainChild"
        )

    private fun createChild(config: ScreenTimeConfig, context: ComponentContext) = when (config) {
        is ScreenTimeConfig.StatsAndLimits -> ScreenTimeComponent.MainChild.ScreenTimeStatsLimits(
            ScreenTimeStatsLimitsComponent(
                componentContext = context,
                isShowingAppStats = isShowingAppStats,
                onShowAppStats = onShowAppStats,
                onExit = onExit
            )
        )
    }
}

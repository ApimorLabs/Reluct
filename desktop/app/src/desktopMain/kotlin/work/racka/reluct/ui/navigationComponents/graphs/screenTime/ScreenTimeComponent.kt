package work.racka.reluct.ui.navigationComponents.graphs.screenTime

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.destination.graphs.AppScreenTimeConfig
import work.racka.reluct.common.core.navigation.destination.graphs.ScreenTimeConfig
import work.racka.reluct.ui.screens.ComposeRenderer
import work.racka.reluct.ui.screens.screenTime.appStats.AppScreenTimeComponent
import work.racka.reluct.ui.screens.screenTime.statsAndLimits.ScreenTimeStatsLimitsComponent

interface ScreenTimeComponent : ComposeRenderer {

    val mainChildStack: Value<ChildStack<ScreenTimeConfig, MainChild>>
    val itemsChildStack: Value<ChildStack<AppScreenTimeConfig, ItemsChild>>

    val isMultipane: Value<Boolean>

    sealed class MainChild {
        class ScreenTimeStatsLimits(val component: ScreenTimeStatsLimitsComponent) : MainChild()
    }

    sealed class ItemsChild {
        class AppScreenTime(val component: AppScreenTimeComponent) : ItemsChild()
        object None : ItemsChild()
    }
}

package work.racka.reluct.ui.navigationComponents.graphs.screenTime

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import work.racka.reluct.common.core.navigation.destination.graphs.AppScreenTimeConfig
import work.racka.reluct.ui.screens.screenTime.appStats.AppScreenTimeComponent

class AppScreenTimeItemsComponentRouter(
    componentContext: ComponentContext,
    initialStack: List<AppScreenTimeConfig>,
    private val isShowingAppStats: Value<Boolean>
) {

    private val navigation = StackNavigation<AppScreenTimeConfig>()

    val stack: Value<ChildStack<AppScreenTimeConfig, ScreenTimeComponent.ItemsChild>> =
        componentContext.childStack(
            source = navigation,
            initialStack = { (listOf(AppScreenTimeConfig.None) + initialStack).distinct() },
            childFactory = ::createChild,
            key = "ScreenTimeItemsChild"
        )

    private fun createChild(config: AppScreenTimeConfig, context: ComponentContext) =
        when (config) {
            is AppScreenTimeConfig.None -> ScreenTimeComponent.ItemsChild.None
            is AppScreenTimeConfig.App -> ScreenTimeComponent.ItemsChild.AppScreenTime(
                AppScreenTimeComponent(
                    componentContext = context,
                    isShowingAppStats = isShowingAppStats,
                    onClose = navigation::pop
                )
            )
        }

    fun openAppStats(packageName: String?) = navigation.navigate { stack ->
        stack.dropLastWhile { it is AppScreenTimeConfig.App }
            .plus(AppScreenTimeConfig.App(packageName = packageName))
    }
}

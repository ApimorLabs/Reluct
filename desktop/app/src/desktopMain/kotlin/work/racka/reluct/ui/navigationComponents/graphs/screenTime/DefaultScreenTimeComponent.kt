package work.racka.reluct.ui.navigationComponents.graphs.screenTime

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import work.racka.reluct.common.core.navigation.destination.graphs.AppScreenTimeConfig
import work.racka.reluct.common.core.navigation.destination.graphs.ScreenTimeConfig
import work.racka.reluct.ui.navigationComponents.graphs.screenTime.ScreenTimeComponent.ItemsChild
import work.racka.reluct.ui.navigationComponents.graphs.screenTime.ScreenTimeComponent.MainChild

class DefaultScreenTimeComponent(
    componentContext: ComponentContext,
    initialItemsStack: () -> List<AppScreenTimeConfig>,
    onExit: () -> Unit
) : ScreenTimeComponent, ComponentContext by componentContext {

    private val _showingAppStats = MutableValue(false)
    override val isMultipane: Value<Boolean> = _showingAppStats

    private val itemsChildRouter = AppScreenTimeItemsComponentRouter(
        componentContext = this,
        initialStack = initialItemsStack(),
        isShowingAppStats = isMultipane
    )

    private val mainChildRouter = MainScreenTimeComponentRouter(
        componentContext = this,
        isShowingAppStats = isMultipane,
        onShowAppStats = itemsChildRouter::openAppStats,
        onExit = onExit
    )

    override val mainChildStack: Value<ChildStack<ScreenTimeConfig, MainChild>> =
        mainChildRouter.stack
    override val itemsChildStack: Value<ChildStack<AppScreenTimeConfig, ItemsChild>> =
        itemsChildRouter.stack

    init {
        itemsChildRouter.stack.subscribe { stack ->
            _showingAppStats.reduce { stack.active.configuration is AppScreenTimeConfig.App }
        }
    }

    @Composable
    override fun Render(modifier: Modifier) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Screen Time Component")
        }
    }
}

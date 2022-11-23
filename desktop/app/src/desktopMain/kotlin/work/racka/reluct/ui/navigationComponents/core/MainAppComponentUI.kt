package work.racka.reluct.ui.navigationComponents.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import work.racka.reluct.common.core.navigation.destination.AppNavConfig
import work.racka.reluct.compose.common.components.animations.slideInVerticallyFadeReversed
import work.racka.reluct.compose.common.components.animations.slideOutVerticallyFadeReversed
import work.racka.reluct.compose.common.components.dialogs.FullScreenLoading
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.ReluctAppTheme
import work.racka.reluct.ui.navigationComponents.navrail.ReluctNavigationRail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppComponentUI(
    rootComponent: MainAppComponent,
    themeValue: State<Int>,
    modifier: Modifier = Modifier
) {
    val stack = rootComponent.childStack.subscribeAsState()
    val activeConfiguration = remember {
        derivedStateOf {
            stack.value.active.configuration
        }
    }

    ReluctAppTheme(theme = themeValue.value) {
        Scaffold(
            modifier = modifier,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
            ) {
                AppNavRail(
                    currentConfig = activeConfiguration,
                    onOpenDashboard = rootComponent::openDashboard,
                    onOpenTasks = rootComponent::openTasks,
                    onOpenScreenTime = rootComponent::openScreenTime,
                    onOpenGoals = rootComponent::openGoals,
                )

                MainAppComponentRootChildren(rootComponent = rootComponent)
            }
        }
    }
}

@Composable
private fun RowScope.AppNavRail(
    currentConfig: State<AppNavConfig>,
    onOpenDashboard: () -> Unit,
    onOpenTasks: () -> Unit,
    onOpenScreenTime: () -> Unit,
    onOpenGoals: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val barsVisible by remember {
        derivedStateOf {
            currentConfig.value !is AppNavConfig.Checking ||
                    currentConfig.value !is AppNavConfig.OnBoarding ||
                    currentConfig.value !is AppNavConfig.Settings
        }
    }

    AnimatedVisibility(
        modifier = modifier.align(Alignment.CenterVertically),
        visible = barsVisible,
        enter = slideInVerticallyFadeReversed(),
        exit = slideOutVerticallyFadeReversed()
    ) {
        ReluctNavigationRail(
            currentConfig = currentConfig,
            onUpdateConfig = { config ->
                when (config) {
                    is AppNavConfig.Dashboard -> onOpenDashboard()
                    is AppNavConfig.Tasks -> onOpenTasks()
                    is AppNavConfig.ScreenTime -> onOpenScreenTime()
                    is AppNavConfig.Goals -> onOpenGoals()
                    else -> onOpenDashboard()
                }
            }
        )
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MainAppComponentRootChildren(
    rootComponent: MainAppComponent,
    modifier: Modifier = Modifier
) {
    Children(
        modifier = modifier.fillMaxSize(),
        stack = rootComponent.childStack,
        animation = stackAnimation(fade() + scale())
    ) { child ->
        when (val childInstance = child.instance) {
            is MainAppComponent.Child.Checking -> FullScreenLoading(isLoadingProvider = { true })
            is MainAppComponent.Child.OnBoarding -> childInstance.component.Render()
            is MainAppComponent.Child.Dashboard -> childInstance.component.Render()
            is MainAppComponent.Child.Tasks -> childInstance.component.Render()
            is MainAppComponent.Child.ScreenTime -> childInstance.component.Render()
            is MainAppComponent.Child.Goals -> childInstance.component.Render()
            is MainAppComponent.Child.Settings -> childInstance.component.Render()
        }
    }
}

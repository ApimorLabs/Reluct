package work.racka.reluct.ui.navigationComponents.graphs.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import work.racka.reluct.common.core.navigation.destination.graphs.GoalsConfig
import work.racka.reluct.common.core.navigation.destination.graphs.GoalsExtrasConfig
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.topBar.ReluctPageHeading
import work.racka.reluct.ui.common.ReluctSplitPane
import work.racka.reluct.ui.navigationComponents.graphs.goals.GoalsComponent.*
import work.racka.reluct.ui.navigationComponents.tabs.ReluctTabBar
import work.racka.reluct.ui.navigationComponents.tabs.goalsTabs

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun GoalsRootChildren(
    mainChildStack: Value<ChildStack<GoalsConfig, MainChild>>,
    itemsChildStack: Value<ChildStack<GoalsExtrasConfig, ItemsChild>>,
    onUpdateConfig: (GoalsConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    ReluctSplitPane(
        modifier = modifier,
        firstSlot = { MainChildStackUI(stack = mainChildStack, onUpdateConfig = onUpdateConfig) },
        secondSlot = { ItemsChildStackUI(stack = itemsChildStack) }
    )
}

@OptIn(ExperimentalDecomposeApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MainChildStackUI(
    stack: Value<ChildStack<GoalsConfig, MainChild>>,
    onUpdateConfig: (GoalsConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val stackState = stack.subscribeAsState()
    val activeConfig = remember { derivedStateOf { stackState.value.active.configuration } }

    Scaffold(
        topBar = { GoalsTopBar(config = activeConfig, updateConfig = onUpdateConfig) }
    ) { inner ->
        Children(
            modifier = modifier.fillMaxSize(),
            stack = stack,
            animation = stackAnimation(fade() + scale())
        ) { child ->
            when (val instance = child.instance) {
                is MainChild.ActiveGoal -> instance.component.Render(Modifier.padding(inner))
                is MainChild.InactiveGoal -> instance.component.Render(Modifier.padding(inner))
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun ItemsChildStackUI(
    stack: Value<ChildStack<GoalsExtrasConfig, ItemsChild>>,
    modifier: Modifier = Modifier
) {
    Children(
        modifier = modifier.fillMaxSize(),
        stack = stack,
        animation = stackAnimation(fade() + scale())
    ) { child ->
        when (val instance = child.instance) {
            is ItemsChild.None -> {}
            is ItemsChild.Details -> instance.component.Render(Modifier)
            is ItemsChild.AddEdit -> instance.component.Render(Modifier)
        }
    }
}

@Composable
private fun GoalsTopBar(
    config: State<GoalsConfig>,
    updateConfig: (GoalsConfig) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReluctPageHeading(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(SharedRes.strings.goals_destination_text),
            extraItems = {}
        )

        LazyRow {
            item {
                ReluctTabBar(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    tabs = goalsTabs,
                    currentTab = config,
                    onTabSelected = updateConfig
                )
            }
        }
    }
}

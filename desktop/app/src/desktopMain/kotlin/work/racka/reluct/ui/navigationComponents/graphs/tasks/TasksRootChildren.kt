package work.racka.reluct.ui.navigationComponents.graphs.tasks

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
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import work.racka.reluct.common.core.navigation.destination.graphs.TasksConfig
import work.racka.reluct.common.core.navigation.destination.graphs.TasksExtraConfig
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.textfields.search.PlaceholderMaterialSearchBar
import work.racka.reluct.ui.common.ReluctSplitPane
import work.racka.reluct.ui.navigationComponents.tabs.TasksTabBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSplitPaneApi::class)
@Composable
fun TasksRootChildren(
    mainChildStack: Value<ChildStack<TasksConfig, TasksComponent.MainChild>>,
    itemsChildStack: Value<ChildStack<TasksExtraConfig, TasksComponent.ItemsChild>>,
    onUpdateConfig: (TasksConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mainChildConfig = remember(mainChildStack.value.active.configuration) {
        derivedStateOf { mainChildStack.value.active.configuration }
    }

    ReluctSplitPane(
        modifier = modifier,
        firstSlot = {
            Scaffold(
                topBar = {
                    TasksScreenTopBar(
                        config = mainChildConfig,
                        updateConfig = onUpdateConfig,
                    )
                }
            ) { innerPapping ->
                MainChildStackUI(
                    stack = mainChildStack,
                    modifier = Modifier.padding(innerPapping)
                )
            }
        },
        secondSlot = { ItemsChildStackUI(stack = itemsChildStack) }
    )
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun MainChildStackUI(
    stack: Value<ChildStack<TasksConfig, TasksComponent.MainChild>>,
    modifier: Modifier = Modifier
) {
    Children(
        modifier = modifier.fillMaxSize(),
        stack = stack,
        animation = stackAnimation(fade() + scale())
    ) { child ->
        when (val instance = child.instance) {
            is TasksComponent.MainChild.Pending -> instance.component.Render(Modifier)
            is TasksComponent.MainChild.Completed -> instance.component.Render(Modifier)
            is TasksComponent.MainChild.Search -> instance.component.Render(Modifier)
            is TasksComponent.MainChild.Statistics -> instance.component.Render(Modifier)
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun ItemsChildStackUI(
    stack: Value<ChildStack<TasksExtraConfig, TasksComponent.ItemsChild>>,
    modifier: Modifier = Modifier
) {
    Children(
        modifier = modifier.fillMaxSize(),
        stack = stack,
        animation = stackAnimation(fade() + scale())
    ) { child ->
        when (val instance = child.instance) {
            is TasksComponent.ItemsChild.None -> {}
            is TasksComponent.ItemsChild.Details -> instance.component.Render(Modifier)
            is TasksComponent.ItemsChild.AddEdit -> instance.component.Render(Modifier)
        }
    }
}

@Composable
private fun TasksScreenTopBar(
    config: State<TasksConfig>,
    updateConfig: (TasksConfig) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        PlaceholderMaterialSearchBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            hint = stringResource(SharedRes.strings.search_tasks_hint_text),
            onClick = { updateConfig(TasksConfig.Search) }
        )
        LazyRow {
            item {
                TasksTabBar(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    currentTab = config,
                    onTabSelected = updateConfig
                )
            }
        }
    }
}

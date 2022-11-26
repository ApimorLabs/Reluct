package work.racka.reluct.ui.navigationComponents.tabs

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import work.racka.reluct.common.core.navigation.destination.graphs.TasksConfig
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.tab.ReluctTabIndicator
import work.racka.reluct.compose.common.components.tab.TabEntry

@Composable
internal fun TasksTabBar(
    currentTab: State<TasksConfig>,
    onTabSelected: (TasksConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = remember {
        derivedStateOf {
            // If the tab is not inside predefined tabs return index 0
            tabs.indexOfFirst { it.first == currentTab.value }.let { if (it == -1) 0 else it }
        }
    }

    TabRow(
        modifier = modifier.width(300.dp),
        selectedTabIndex = selectedIndex.value,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            ReluctTabIndicator(currentTabPosition = tabPositions[selectedIndex.value])
        },
        divider = { }
    ) {
        tabs.forEach { tab ->
            TabEntry(
                title = stringResource(tab.second),
                textColor = getTasksTabTextColor(isSelected = tab.first == currentTab.value),
                onClick = { onTabSelected(tab.first) }
            )
        }
    }
}

private val tabs = listOf(
    Pair(TasksConfig.Pending, SharedRes.strings.tasks_pending),
    Pair(TasksConfig.Completed, SharedRes.strings.tasks_done),
    Pair(TasksConfig.Statistics, SharedRes.strings.tasks_stats),
)

@Composable
private fun getTasksTabTextColor(
    isSelected: Boolean,
): Color = if (isSelected) {
    MaterialTheme.colorScheme.onPrimary
} else {
    MaterialTheme.colorScheme.onBackground
        .copy(alpha = .5f)
}

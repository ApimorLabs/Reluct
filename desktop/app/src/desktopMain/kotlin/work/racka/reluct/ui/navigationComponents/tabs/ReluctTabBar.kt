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
import dev.icerock.moko.resources.StringResource
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.tab.ReluctTabIndicator
import work.racka.reluct.compose.common.components.tab.TabEntry

@Composable
internal fun <T> ReluctTabBar(
    tabs: Array<Pair<T, StringResource>>,
    currentTab: State<T>,
    onTabSelected: (T) -> Unit,
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

@Composable
private fun getTasksTabTextColor(
    isSelected: Boolean,
): Color = if (isSelected) {
    MaterialTheme.colorScheme.onPrimary
} else {
    MaterialTheme.colorScheme.onBackground
        .copy(alpha = .5f)
}

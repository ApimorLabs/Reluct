package work.racka.reluct.android.compose.navigation.toptabs.tasks

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import work.racka.reluct.android.compose.components.tab.TabEntry
import work.racka.reluct.android.compose.navigation.R

@Composable
fun TasksTabBar(
    tabPage: TasksTabDestination,
    onTabSelected: (tabPage: TasksTabDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        modifier = modifier.width(300.dp),
        selectedTabIndex = tabPage.ordinal,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            TasksTabIndicator(
                tabPositions = tabPositions.toImmutableList(),
                tabPage = tabPage
            )
        },
        divider = { }
    ) {
        TabEntry(
            title = stringResource(id = R.string.tasks_pending),
            textColor = getTasksTabTextColor(
                tabPage = TasksTabDestination.Tasks,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(TasksTabDestination.Tasks)
            }
        )
        TabEntry(
            title = stringResource(id = R.string.tasks_done),
            textColor = getTasksTabTextColor(
                tabPage = TasksTabDestination.Done,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(TasksTabDestination.Done)
            }
        )
        TabEntry(
            title = stringResource(id = R.string.tasks_stats),
            textColor = getTasksTabTextColor(
                tabPage = TasksTabDestination.Statistics,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(TasksTabDestination.Statistics)
            }
        )
    }
}

@Composable
private fun getTasksTabTextColor(
    tabPage: TasksTabDestination,
    selectedTabPage: TasksTabDestination,
): Color =
    if (tabPage == selectedTabPage) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
            .copy(alpha = .5f)
    }

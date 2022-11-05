package work.racka.reluct.android.compose.navigation.toptabs.tasks

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.tab.ReluctTabIndicator
import work.racka.reluct.android.compose.components.tab.TabEntry
import work.racka.reluct.android.compose.navigation.R

@Composable
fun TasksTabBar(
    tabPage: State<TasksTabDestination>,
    onTabSelected: (tabPage: TasksTabDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        modifier = modifier.width(300.dp),
        selectedTabIndex = tabPage.value.ordinal,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            ReluctTabIndicator(currentTabPosition = tabPositions[tabPage.value.ordinal])
        },
        divider = { }
    ) {
        TabEntry(
            title = stringResource(id = R.string.tasks_pending),
            textColor = getTasksTabTextColor(
                tabPage = TasksTabDestination.Tasks,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(TasksTabDestination.Tasks)
            }
        )
        TabEntry(
            title = stringResource(id = R.string.tasks_done),
            textColor = getTasksTabTextColor(
                tabPage = TasksTabDestination.Done,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(TasksTabDestination.Done)
            }
        )
        TabEntry(
            title = stringResource(id = R.string.tasks_stats),
            textColor = getTasksTabTextColor(
                tabPage = TasksTabDestination.Statistics,
                selectedTabPage = tabPage.value
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

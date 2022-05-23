package work.racka.reluct.android.compose.components.tab.tasks

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.tab.TabEntry
import work.racka.reluct.android.compose.destinations.TasksDestinations

@Composable
fun TasksTabBar(
    modifier: Modifier = Modifier,
    tabPage: TasksDestinations,
    onTabSelected: (tabPage: TasksDestinations) -> Unit,
) {
    TabRow(
        modifier = modifier.width(300.dp),
        selectedTabIndex = tabPage.ordinal,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            TasksTabIndicator(
                tabPositions = tabPositions,
                tabPage = tabPage
            )
        },
        divider = { }
    ) {
        TabEntry(
            title = TasksDestinations.Tasks.label,
            textColor = getTasksTabTextColor(
                tabPage = TasksDestinations.Tasks,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(TasksDestinations.Tasks)
            }
        )
        TabEntry(
            title = TasksDestinations.Done.label,
            textColor = getTasksTabTextColor(
                tabPage = TasksDestinations.Done,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(TasksDestinations.Done)
            }
        )
        TabEntry(
            title = TasksDestinations.Statistics.label,
            textColor = getTasksTabTextColor(
                tabPage = TasksDestinations.Statistics,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(TasksDestinations.Statistics)
            }
        )
    }
}

@Composable
private fun getTasksTabTextColor(
    tabPage: TasksDestinations,
    selectedTabPage: TasksDestinations,
): Color =
    if (tabPage == selectedTabPage) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onBackground
        .copy(alpha = .5f)
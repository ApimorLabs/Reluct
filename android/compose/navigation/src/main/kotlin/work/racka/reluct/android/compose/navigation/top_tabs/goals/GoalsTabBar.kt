package work.racka.reluct.android.compose.navigation.top_tabs.goals

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.tab.TabEntry
import work.racka.reluct.android.compose.navigation.R

@Composable
fun GoalsTabBar(
    modifier: Modifier = Modifier,
    tabPage: GoalsTabDestination,
    onTabSelected: (tabPage: GoalsTabDestination) -> Unit,
) {
    TabRow(
        modifier = modifier.width(250.dp),
        selectedTabIndex = tabPage.ordinal,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            GoalsTabIndicator(
                tabPositions = tabPositions,
                tabPage = tabPage
            )
        },
        divider = { }
    ) {
        TabEntry(
            title = stringResource(id = R.string.goals_ongoing),
            textColor = getGoalsTabTextColor(
                tabPage = GoalsTabDestination.Ongoing,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(GoalsTabDestination.Ongoing)
            }
        )
        TabEntry(
            title = stringResource(id = R.string.goals_completed),
            textColor = getGoalsTabTextColor(
                tabPage = GoalsTabDestination.Completed,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(GoalsTabDestination.Completed)
            }
        )
    }
}

@Composable
private fun getGoalsTabTextColor(
    tabPage: GoalsTabDestination,
    selectedTabPage: GoalsTabDestination,
): Color =
    if (tabPage == selectedTabPage) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onBackground
        .copy(alpha = .5f)
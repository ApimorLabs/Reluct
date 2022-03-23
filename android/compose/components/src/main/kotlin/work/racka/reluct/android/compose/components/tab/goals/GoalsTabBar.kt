package work.racka.reluct.android.compose.components.tab.goals

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.tab.TabEntry
import work.racka.reluct.common.compose.destinations.GoalsDestinations

@Composable
fun GoalsTabBar(
    modifier: Modifier = Modifier,
    tabPage: GoalsDestinations,
    onTabSelected: (tabPage: GoalsDestinations) -> Unit
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
            title = GoalsDestinations.Ongoing.label,
            textColor = getGoalsTabTextColor(
                tabPage = GoalsDestinations.Ongoing,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(GoalsDestinations.Ongoing)
            }
        )
        TabEntry(
            title = GoalsDestinations.Completed.label,
            textColor = getGoalsTabTextColor(
                tabPage = GoalsDestinations.Completed,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(GoalsDestinations.Completed)
            }
        )
    }
}

@Composable
private fun getGoalsTabTextColor(
    tabPage: GoalsDestinations,
    selectedTabPage: GoalsDestinations
): Color =
    if (tabPage == selectedTabPage) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onBackground
        .copy(alpha = .5f)
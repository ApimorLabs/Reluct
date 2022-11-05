package work.racka.reluct.android.compose.navigation.toptabs.goals

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
fun GoalsTabBar(
    tabPage: State<GoalsTabDestination>,
    onTabSelected: (tabPage: GoalsTabDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        modifier = modifier.width(250.dp),
        selectedTabIndex = tabPage.value.ordinal,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            ReluctTabIndicator(currentTabPosition = tabPositions[tabPage.value.ordinal])
        },
        divider = { }
    ) {
        TabEntry(
            title = stringResource(id = R.string.goals_active),
            textColor = getGoalsTabTextColor(
                tabPage = GoalsTabDestination.Active,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(GoalsTabDestination.Active)
            }
        )
        TabEntry(
            title = stringResource(id = R.string.goals_inactive),
            textColor = getGoalsTabTextColor(
                tabPage = GoalsTabDestination.Inactive,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(GoalsTabDestination.Inactive)
            }
        )
    }
}

@Composable
private fun getGoalsTabTextColor(
    tabPage: GoalsTabDestination,
    selectedTabPage: GoalsTabDestination,
): Color =
    if (tabPage == selectedTabPage) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
            .copy(alpha = .5f)
    }

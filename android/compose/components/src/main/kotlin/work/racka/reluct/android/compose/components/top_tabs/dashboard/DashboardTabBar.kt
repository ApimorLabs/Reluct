package work.racka.reluct.android.compose.components.top_tabs.dashboard

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.tab.TabEntry
import work.racka.reluct.android.compose.theme.ReluctAppTheme

@Composable
fun DashboardTabBar(
    modifier: Modifier = Modifier,
    tabPage: DashboardTabDestination,
    onTabSelected: (tabPage: DashboardTabDestination) -> Unit,
) {
    TabRow(
        modifier = modifier.width(250.dp),
        selectedTabIndex = tabPage.ordinal,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            DashboardTabIndicator(
                tabPositions = tabPositions,
                tabPage = tabPage
            )
        },
        divider = { }
    ) {
        TabEntry(
            title = "Overview",
            textColor = getDashboardTabTextColor(
                tabPage = DashboardTabDestination.Overview,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(DashboardTabDestination.Overview)
            }
        )
        TabEntry(
            title = "Statistics",
            textColor = getDashboardTabTextColor(
                tabPage = DashboardTabDestination.Statistics,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(DashboardTabDestination.Statistics)
            }
        )
    }
}

@Preview
@Composable
private fun DashTabPrev() {
    ReluctAppTheme {
        DashboardTabBar(
            tabPage = DashboardTabDestination.Overview,
            onTabSelected = {}
        )
    }
}

@Composable
private fun getDashboardTabTextColor(
    tabPage: DashboardTabDestination,
    selectedTabPage: DashboardTabDestination,
): Color =
    if (tabPage == selectedTabPage) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onBackground
        .copy(alpha = .5f)
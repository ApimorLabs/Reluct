package work.racka.reluct.android.compose.components.tab.dashboard

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
import work.racka.reluct.common.compose.destinations.DashboardDestinations

@Composable
fun DashboardTabBar(
    modifier: Modifier = Modifier,
    tabPage: DashboardDestinations,
    onTabSelected: (tabPage: DashboardDestinations) -> Unit
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
            title = DashboardDestinations.Overview.label,
            textColor = getDashboardTabTextColor(
                tabPage = DashboardDestinations.Overview,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(DashboardDestinations.Overview)
            }
        )
        TabEntry(
            title = DashboardDestinations.Statistics.label,
            textColor = getDashboardTabTextColor(
                tabPage = DashboardDestinations.Statistics,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(DashboardDestinations.Statistics)
            }
        )
    }
}

@Preview
@Composable
private fun DashTabPrev() {
    ReluctAppTheme {
        DashboardTabBar(
            tabPage = DashboardDestinations.Overview,
            onTabSelected = {}
        )
    }
}

@Composable
private fun getDashboardTabTextColor(
    tabPage: DashboardDestinations,
    selectedTabPage: DashboardDestinations
): Color =
    if (tabPage == selectedTabPage) Color.White
    else MaterialTheme.colorScheme.onBackground
        .copy(alpha = .5f)
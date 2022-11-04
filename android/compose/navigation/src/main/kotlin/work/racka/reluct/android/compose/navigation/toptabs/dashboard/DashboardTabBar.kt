package work.racka.reluct.android.compose.navigation.toptabs.dashboard

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import work.racka.reluct.android.compose.components.tab.TabEntry
import work.racka.reluct.android.compose.navigation.R
import work.racka.reluct.android.compose.theme.ReluctAppTheme

@Composable
fun DashboardTabBar(
    tabPage: DashboardTabDestination,
    onTabSelected: (tabPage: DashboardTabDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        modifier = modifier.width(250.dp),
        selectedTabIndex = tabPage.ordinal,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            DashboardTabIndicator(
                tabPositions = tabPositions.toImmutableList(),
                tabPage = tabPage
            )
        },
        divider = { }
    ) {
        TabEntry(
            title = stringResource(id = R.string.dashboard_overview),
            textColor = getDashboardTabTextColor(
                tabPage = DashboardTabDestination.Overview,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(DashboardTabDestination.Overview)
            }
        )
        TabEntry(
            title = stringResource(id = R.string.dashboard_statistics),
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
    if (tabPage == selectedTabPage) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
            .copy(alpha = .5f)
    }

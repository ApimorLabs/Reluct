package work.racka.reluct.android.navigation.toptabs.dashboard

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.navigation.R
import work.racka.reluct.compose.common.components.tab.ReluctTabIndicator
import work.racka.reluct.compose.common.components.tab.TabEntry
import work.racka.reluct.compose.common.theme.ReluctAppTheme

@Composable
fun DashboardTabBar(
    tabPage: State<DashboardTabDestination>,
    onTabSelected: (tabPage: DashboardTabDestination) -> Unit,
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
            title = stringResource(id = R.string.dashboard_overview),
            textColor = getDashboardTabTextColor(
                tabPage = DashboardTabDestination.Overview,
                selectedTabPage = tabPage.value
            ),
            onClick = {
                onTabSelected(DashboardTabDestination.Overview)
            }
        )
        TabEntry(
            title = stringResource(id = R.string.dashboard_statistics),
            textColor = getDashboardTabTextColor(
                tabPage = DashboardTabDestination.Statistics,
                selectedTabPage = tabPage.value
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
        val page = remember { mutableStateOf(DashboardTabDestination.Overview) }
        DashboardTabBar(
            tabPage = page,
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

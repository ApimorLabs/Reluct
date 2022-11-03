package work.racka.reluct.android.compose.navigation.toptabs.screentime

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
fun ScreenTimeTabBar(
    tabPage: ScreenTimeTabDestination,
    onTabSelected: (tabPage: ScreenTimeTabDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        modifier = modifier.width(250.dp),
        selectedTabIndex = tabPage.ordinal,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            ScreenTimeTabIndicator(
                tabPositions = tabPositions.toImmutableList(),
                tabPage = tabPage
            )
        },
        divider = { }
    ) {
        TabEntry(
            title = stringResource(id = R.string.screen_time_stats),
            textColor = getScreenTimeTabTextColor(
                tabPage = ScreenTimeTabDestination.Statistics,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(ScreenTimeTabDestination.Statistics)
            }
        )
        TabEntry(
            title = stringResource(id = R.string.screen_time_limit),
            textColor = getScreenTimeTabTextColor(
                tabPage = ScreenTimeTabDestination.Limits,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(ScreenTimeTabDestination.Limits)
            }
        )
    }
}

@Composable
private fun getScreenTimeTabTextColor(
    tabPage: ScreenTimeTabDestination,
    selectedTabPage: ScreenTimeTabDestination,
): Color =
    if (tabPage == selectedTabPage) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
            .copy(alpha = .5f)
    }

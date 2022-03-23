package work.racka.reluct.android.compose.components.tab.screentime

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.tab.TabEntry
import work.racka.reluct.common.compose.destinations.ScreenTimeDestinations

@Composable
fun ScreenTimeTabBar(
    modifier: Modifier = Modifier,
    tabPage: ScreenTimeDestinations,
    onTabSelected: (tabPage: ScreenTimeDestinations) -> Unit
) {
    TabRow(
        modifier = modifier.width(250.dp),
        selectedTabIndex = tabPage.ordinal,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            ScreenTimeTabIndicator(
                tabPositions = tabPositions,
                tabPage = tabPage
            )
        },
        divider = { }
    ) {
        TabEntry(
            title = ScreenTimeDestinations.Statistics.label,
            textColor = getScreenTimeTabTextColor(
                tabPage = ScreenTimeDestinations.Statistics,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(ScreenTimeDestinations.Statistics)
            }
        )
        TabEntry(
            title = ScreenTimeDestinations.Limits.label,
            textColor = getScreenTimeTabTextColor(
                tabPage = ScreenTimeDestinations.Limits,
                selectedTabPage = tabPage
            ),
            onClick = {
                onTabSelected(ScreenTimeDestinations.Limits)
            }
        )
    }
}

@Composable
private fun getScreenTimeTabTextColor(
    tabPage: ScreenTimeDestinations,
    selectedTabPage: ScreenTimeDestinations
): Color =
    if (tabPage == selectedTabPage) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onBackground
        .copy(alpha = .5f)
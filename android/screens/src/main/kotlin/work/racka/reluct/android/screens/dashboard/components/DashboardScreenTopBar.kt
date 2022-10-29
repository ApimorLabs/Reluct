package work.racka.reluct.android.screens.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.tab.TabEntry
import work.racka.reluct.android.compose.components.topBar.ReluctPageHeading
import work.racka.reluct.android.compose.components.top_tabs.dashboard.DashboardTabDestination

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun DashboardScreenTopBar(
    pagerState: PagerState,
    //profilePicUrl: String?,
    updateTabPage: (DashboardTabDestination) -> Unit,
    onSettingsClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .statusBarsPadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        ReluctPageHeading(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(id = R.string.dashboard_destination_text),
            extraItems = {
                IconButton(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        CircleShape
                    ),
                    onClick = onSettingsClicked,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings"
                    )
                }
                /*ProfilePicture(
                    modifier = Modifier,//.padding(4.dp),
                    pictureUrl = profilePicUrl,
                    size = 36.dp
                )*/
            }
        )

        LazyRow {
            item {
                TabRow(
                    modifier = Modifier.width(250.dp),
                    selectedTabIndex = pagerState.currentPage,
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    indicator = { tabPositions ->
                        Box(
                            modifier = Modifier
                                .pagerTabIndicatorOffset(pagerState, tabPositions)
                                .zIndex(1f)
                                .wrapContentSize(align = Alignment.CenterStart)
                                .fillMaxSize()
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                        )
                    },
                    divider = { }
                ) {
                    DashboardTabDestination.values().forEach { item ->
                        TabEntry(
                            title = stringResource(id = item.titleResId),
                            textColor = getTabTextColor(
                                selectedIndex = pagerState.currentPage,
                                itemIndex = item.ordinal
                            ),
                            onClick = {
                                updateTabPage(item)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun getTabTextColor(
    selectedIndex: Int,
    itemIndex: Int
): Color =
    if (selectedIndex == itemIndex) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onBackground
        .copy(alpha = .5f)
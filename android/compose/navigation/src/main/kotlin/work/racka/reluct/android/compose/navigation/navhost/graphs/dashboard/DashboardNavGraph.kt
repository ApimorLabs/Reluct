package work.racka.reluct.android.compose.navigation.navhost.graphs.dashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable
import timber.log.Timber
import work.racka.reluct.android.compose.components.ComponentsPreview
import work.racka.reluct.android.compose.components.search.ReluctSearchBar
import work.racka.reluct.android.compose.components.tab.dashboard.DashboardTabBar
import work.racka.reluct.android.compose.components.topBar.ProfilePicture
import work.racka.reluct.common.compose.destinations.DashboardDestinations
import work.racka.reluct.common.compose.destinations.navbar.Graphs

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@ExperimentalAnimationApi
internal fun NavGraphBuilder.dashboardNavGraph(
    navController: NavHostController,
) {
    navigation(
        route = Graphs.DashboardDestinations.route,
        startDestination = DashboardDestinations.Overview.route
    ) {
        Timber.d("Dashboard screen called")
        // Overview
        composable(
            route = DashboardDestinations.Overview.route
        ) {
            val tabPage = remember {
                mutableStateOf(DashboardDestinations.Overview)
            }
            Scaffold(
                topBar = {
                    LazyColumn {
                        item {
                            ReluctSearchBar(
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(vertical = 16.dp),
                                extraButton = {
                                    ProfilePicture(
                                        modifier = Modifier,//.padding(4.dp),
                                        pictureUrl = null
                                    )
                                }
                            )
                        }
                        item {
                            DashboardTabBar(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp),
                                tabPage = tabPage.value,
                                onTabSelected = { tabPage.value = it }
                            )
                        }
                    }
                }
            ) {
                val scrollState = rememberScrollState()
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        Modifier
                            .fillMaxWidth(.9f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            ComponentsPreview()
                        }

                        item {
                            Text(
                                text = "Dashboard: $route",
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }

        // Statistics
        composable(
            route = DashboardDestinations.Statistics.route
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Dashboard: $route",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
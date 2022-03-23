package work.racka.reluct.android.compose.navigation.navhost.graphs.dashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.navigation.animation.composable
import timber.log.Timber
import work.racka.reluct.android.compose.components.tab.dashboard.DashboardTabBar
import work.racka.reluct.android.compose.navigation.destinations.Graphs
import work.racka.reluct.common.compose.destinations.DashboardDestinations

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
internal fun NavGraphBuilder.dashboardNavGraph(
    navController: NavHostController
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
                    LazyRow {
                        item {
                            DashboardTabBar(
                                modifier = Modifier
                                    .statusBarsPadding(),
                                tabPage = tabPage.value,
                                onTabSelected = { tabPage.value = it }
                            )
                        }
                    }
                }
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
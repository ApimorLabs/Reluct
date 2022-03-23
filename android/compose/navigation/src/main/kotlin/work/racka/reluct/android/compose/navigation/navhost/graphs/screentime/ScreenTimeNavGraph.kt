package work.racka.reluct.android.compose.navigation.navhost.graphs.screentime

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
import work.racka.reluct.android.compose.components.tab.screentime.ScreenTimeTabBar
import work.racka.reluct.android.compose.navigation.destinations.Graphs
import work.racka.reluct.common.compose.destinations.ScreenTimeDestinations

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
internal fun NavGraphBuilder.screenTimeNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.ScreenTimeDestinations.route,
        startDestination = ScreenTimeDestinations.Statistics.route
    ) {
        Timber.d("Statistics screen called")
        // Statistics
        composable(
            route = ScreenTimeDestinations.Statistics.route
        ) {
            val tabPage = remember {
                mutableStateOf(ScreenTimeDestinations.Statistics)
            }
            Scaffold(
                topBar = {
                    LazyRow {
                        item {
                            ScreenTimeTabBar(
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
                        text = "Screen Time: $route",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Limits
        composable(
            route = ScreenTimeDestinations.Limits.route
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Screen Time: $route",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
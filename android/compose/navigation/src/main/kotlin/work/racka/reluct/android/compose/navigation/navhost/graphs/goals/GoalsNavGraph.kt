package work.racka.reluct.android.compose.navigation.navhost.graphs.goals

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.google.accompanist.navigation.animation.composable
import timber.log.Timber
import work.racka.reluct.android.compose.destinations.GoalsDestinations
import work.racka.reluct.android.compose.navigation.navbar.NavbarDestinations
import work.racka.reluct.android.compose.navigation.top_tabs.goals.GoalsTabBar

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
internal fun NavGraphBuilder.goalsNavGraph(
    navController: NavHostController,
) {
    navigation(
        route = NavbarDestinations.Goals.route,
        startDestination = GoalsDestinations.Ongoing.route
    ) {
        Timber.d("Goals screen called")
        // Ongoing
        composable(
            route = GoalsDestinations.Ongoing.route
        ) {
            val tabPage = remember {
                mutableStateOf(GoalsDestinations.Ongoing)
            }
            Scaffold(
                topBar = {
                    LazyRow {
                        item {
                            GoalsTabBar(
                                modifier = Modifier
                                    .statusBarsPadding(),
                                tabPage = tabPage.value,
                                onTabSelected = { tabPage.value = it }
                            )
                        }
                    }
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Goals: $route",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Completed
        composable(
            route = GoalsDestinations.Completed.route
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Goals: $route",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
package work.racka.reluct.android.compose.navigation.navhost.graphs.goals

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable
import timber.log.Timber
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.animations.slideInVerticallyFadeReversed
import work.racka.reluct.android.compose.components.animations.slideOutVerticallyFadeReversed
import work.racka.reluct.android.compose.components.topBar.ReluctPageHeading
import work.racka.reluct.android.compose.navigation.navbar.NavbarDestinations
import work.racka.reluct.android.compose.navigation.top_tabs.goals.GoalsTabBar
import work.racka.reluct.android.compose.navigation.top_tabs.goals.GoalsTabDestination
import work.racka.reluct.common.core_navigation.compose_destinations.goals.CompletedGoalsDestination
import work.racka.reluct.common.core_navigation.compose_destinations.goals.OngoingGoalsDestination

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
internal fun NavGraphBuilder.goalsNavGraph(
    navController: NavHostController,
) {
    val transScale = .05f

    navigation(
        route = NavbarDestinations.Goals.route,
        startDestination = OngoingGoalsDestination.route
    ) {
        // Ongoing
        composable(
            route = OngoingGoalsDestination.route,
            enterTransition = { slideInVerticallyFadeReversed(initialScale = transScale) },
            exitTransition = { slideOutVerticallyFadeReversed(targetScale = transScale) },
            popEnterTransition = { slideInVerticallyFadeReversed(initialScale = transScale) },
            popExitTransition = { slideOutVerticallyFadeReversed(targetScale = transScale) }
        ) {
            Timber.d("Goals screen called")
            val tabPage = remember {
                mutableStateOf(GoalsTabDestination.Ongoing)
            }
            Scaffold(
                topBar = {
                    GoalsTopBar(
                        tabPage = tabPage.value,
                        updateTabPage = { tabPage.value = it }
                    )
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
            route = CompletedGoalsDestination.route,
            enterTransition = { slideInVerticallyFadeReversed(initialScale = transScale) },
            exitTransition = { slideOutVerticallyFadeReversed(targetScale = transScale) },
            popEnterTransition = { slideInVerticallyFadeReversed(initialScale = transScale) },
            popExitTransition = { slideOutVerticallyFadeReversed(targetScale = transScale) }
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

@Composable
private fun GoalsTopBar(
    tabPage: GoalsTabDestination,
    updateTabPage: (GoalsTabDestination) -> Unit,
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
            title = stringResource(id = R.string.goals_destination_text),
            extraItems = {}
        )

        LazyRow {
            item {
                GoalsTabBar(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    tabPage = tabPage,
                    onTabSelected = {
                        updateTabPage(it)
                    }
                )
            }
        }
    }
}

private fun getCurrentTab(currentDestination: NavDestination?): GoalsTabDestination {
    val destinations = GoalsTabDestination.values()
    destinations.forEach { item ->
        val isSelected = currentDestination?.hierarchy?.any {
            it.route == item.route
        } ?: false
        if (isSelected) return item
    }
    return destinations.first()
}
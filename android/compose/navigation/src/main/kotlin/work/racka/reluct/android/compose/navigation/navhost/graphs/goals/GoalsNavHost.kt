package work.racka.reluct.android.compose.navigation.navhost.graphs.goals

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.topBar.ReluctPageHeading
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.navigation.top_tabs.goals.GoalsTabBar
import work.racka.reluct.android.compose.navigation.top_tabs.goals.GoalsTabDestination
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.compose.navigation.util.NavHelpers.navigateNavBarElements
import work.racka.reluct.android.screens.goals.active.ActiveGoalsScreen
import work.racka.reluct.android.screens.goals.inactive.InactiveGoalsScreen
import work.racka.reluct.common.core_navigation.compose_destinations.goals.ActiveGoalsDestination
import work.racka.reluct.common.core_navigation.compose_destinations.goals.AddEditGoalDestination
import work.racka.reluct.common.core_navigation.compose_destinations.goals.GoalDetailsDestination
import work.racka.reluct.common.core_navigation.compose_destinations.goals.InactiveGoalsDestination

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
internal fun GoalsNavHost(
    mainNavController: NavHostController,
    barsVisibility: BarsVisibility,
    navController: NavHostController = rememberAnimatedNavController(),
    mainScaffoldPadding: PaddingValues
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val tabPage by remember(currentDestination) {
        derivedStateOf {
            getCurrentTab(currentDestination)
        }
    }

    Scaffold(
        topBar = {
            GoalsTopBar(
                tabPage = tabPage,
                updateTabPage = {
                    navController.navigateNavBarElements(it.route)
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        AnimatedNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            route = ActiveGoalsDestination.destination,
            startDestination = ActiveGoalsDestination.route
        ) {
            // Active
            composable(
                route = ActiveGoalsDestination.route,
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                ActiveGoalsScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAddGoal = { defaultGoalIndex: Int? ->
                        mainNavController.navigate(
                            AddEditGoalDestination.argsRoute(
                                goalId = null,
                                defaultGoalIndex = defaultGoalIndex
                            )
                        )
                    },
                    onNavigateToGoalDetails = { goalId: String? ->
                        mainNavController.navigate(GoalDetailsDestination.argsRoute(goalId))
                    }
                )
            }

            // Inactive
            composable(
                route = InactiveGoalsDestination.route,
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                InactiveGoalsScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAddGoal = { defaultGoalIndex: Int? ->
                        mainNavController.navigate(
                            AddEditGoalDestination.argsRoute(
                                goalId = null,
                                defaultGoalIndex = defaultGoalIndex
                            )
                        )
                    },
                    onNavigateToGoalDetails = { goalId: String? ->
                        mainNavController.navigate(GoalDetailsDestination.argsRoute(goalId))
                    }
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
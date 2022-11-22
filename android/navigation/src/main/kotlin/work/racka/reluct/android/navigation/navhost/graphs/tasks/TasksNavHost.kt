package work.racka.reluct.android.navigation.navhost.graphs.tasks

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
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
import work.racka.reluct.android.navigation.R
import work.racka.reluct.android.navigation.toptabs.tasks.TasksTabBar
import work.racka.reluct.android.navigation.toptabs.tasks.TasksTabDestination
import work.racka.reluct.android.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.navigation.util.NavHelpers.navigateNavBarElements
import work.racka.reluct.android.screens.tasks.done.CompletedTasksScreen
import work.racka.reluct.android.screens.tasks.pending.PendingTasksScreen
import work.racka.reluct.android.screens.tasks.statistics.TasksStatisticsScreen
import work.racka.reluct.common.core.navigation.composeDestinations.tasks.*
import work.racka.reluct.compose.common.components.textfields.search.PlaceholderMaterialSearchBar
import work.racka.reluct.compose.common.components.util.BarsVisibility

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
internal fun TasksNavHost(
    mainNavController: NavHostController,
    barsVisibility: BarsVisibility,
    mainScaffoldPadding: PaddingValues,
    navController: NavHostController = rememberAnimatedNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val tabPage = remember(currentDestination) {
        derivedStateOf {
            getCurrentTab(currentDestination)
        }
    }

    Scaffold(
        topBar = {
            TasksScreenTopBar(
                tabPage = tabPage,
                navigateToSearch = { mainNavController.navigate(SearchTasksDestination.route) },
                updateTabPage = {
                    navController.navigateNavBarElements(it.route)
                },
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPapping ->
        AnimatedNavHost(
            modifier = Modifier.padding(innerPapping),
            navController = navController,
            route = PendingTasksDestination.destination,
            startDestination = PendingTasksDestination.route
        ) {
            // Tasks
            composable(
                route = PendingTasksDestination.route,
                // Transition animations
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                PendingTasksScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAddTask = {
                        mainNavController.navigate(
                            AddEditTaskDestination.argsRoute(it)
                        )
                    },
                    onNavigateToTaskDetails = {
                        mainNavController.navigate(
                            TaskDetailsDestination.argsRoute(it)
                        )
                    }
                )
            }

            // Done - Completed Tasks
            composable(
                route = CompletedTasksDestination.route,
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                CompletedTasksScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToAddTask = {
                        mainNavController.navigate(
                            AddEditTaskDestination.argsRoute(it)
                        )
                    },
                    onNavigateToTaskDetails = {
                        mainNavController.navigate(
                            TaskDetailsDestination.argsRoute(it)
                        )
                    }
                )
            }
            // Statistics
            composable(
                route = TasksStatisticsDestination.route,
                enterTransition = { scaleInEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                popExitTransition = { scaleOutPopExitTransition() }
            ) {
                TasksStatisticsScreen(
                    mainScaffoldPadding = mainScaffoldPadding,
                    barsVisibility = barsVisibility,
                    onNavigateToTaskDetails = {
                        mainNavController.navigate(
                            TaskDetailsDestination.argsRoute(it)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TasksScreenTopBar(
    tabPage: State<TasksTabDestination>,
    navigateToSearch: () -> Unit,
    updateTabPage: (TasksTabDestination) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .statusBarsPadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        PlaceholderMaterialSearchBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            hint = stringResource(id = R.string.search_tasks_hint_text),
            onClick = navigateToSearch
        )
        LazyRow {
            item {
                TasksTabBar(
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

private fun getCurrentTab(currentDestination: NavDestination?): TasksTabDestination {
    val destinations = TasksTabDestination.values()
    destinations.forEach { item ->
        val isSelected = currentDestination?.hierarchy?.any {
            it.route == item.route
        } ?: false
        if (isSelected) return item
    }
    return destinations.first()
}

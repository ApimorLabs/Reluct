package work.racka.reluct.android.compose.navigation.navhost.graphs.tasks

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import work.racka.reluct.android.compose.components.tab.tasks.TasksTabBar
import work.racka.reluct.android.compose.components.textfields.search.PlaceholderMaterialSearchBar
import work.racka.reluct.android.compose.components.topBar.ProfilePicture
import work.racka.reluct.android.compose.components.topBar.ReluctContentTopBar
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.destinations.OtherDestinations
import work.racka.reluct.android.compose.destinations.TasksDestinations
import work.racka.reluct.android.compose.destinations.navbar.Graphs
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.screens.tasks.done.CompletedTasksScreen
import work.racka.reluct.android.screens.tasks.pending.PendingTasksScreen
import work.racka.reluct.android.screens.tasks.statistics.TasksStatisticsScreen

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
internal fun TasksNavHost(
    mainNavController: NavHostController,
    barsVisibility: BarsVisibility,
    navController: NavHostController = rememberAnimatedNavController(),
    mainScaffoldPadding: PaddingValues,
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
            TasksScreenTopBar(
                tabPage = tabPage,
                profilePicUrl = "https://pbs.twimg.com/profile_images/1451052243067805698/LIEt076e_400x400.jpg",
                navigateToSearch = { mainNavController.navigate(OtherDestinations.SearchTasks.route) },
                updateTabPage = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    ) { innerPapping ->
        AnimatedNavHost(
            modifier = Modifier.padding(innerPapping),
            navController = navController,
            route = Graphs.RootDestinations.route + Graphs.TasksDestinations.route,
            startDestination = TasksDestinations.Tasks.route
        ) {

            // Tasks
            composable(
                route = TasksDestinations.Tasks.route,
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
                            "${OtherDestinations.AddEditTask.route}/$it"
                        )
                    },
                    onNavigateToTaskDetails = {
                        mainNavController.navigate(
                            "${OtherDestinations.TaskDetails.route}/$it"
                        )
                    }
                )
            }

            // Done - Completed Tasks
            composable(
                route = TasksDestinations.Done.route,
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
                            "${OtherDestinations.AddEditTask.route}/$it"
                        )
                    },
                    onNavigateToTaskDetails = {
                        mainNavController.navigate(
                            "${OtherDestinations.TaskDetails.route}/$it"
                        )
                    }
                )
            }
            // Statistics
            composable(
                route = TasksDestinations.Statistics.route,
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
                            "${OtherDestinations.TaskDetails.route}/$it"
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TasksScreenTopBar(
    tabPage: TasksDestinations,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    profilePicUrl: String?,
    navigateToSearch: () -> Unit,
    updateTabPage: (TasksDestinations) -> Unit,
) {
    ReluctContentTopBar(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .statusBarsPadding(),
        minShrinkHeight = 40.dp,
        scrollBehavior = scrollBehavior
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement
                .spacedBy(16.dp)
        ) {
            PlaceholderMaterialSearchBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = navigateToSearch,
                extraButton = {
                    ProfilePicture(
                        modifier = Modifier,//.padding(4.dp),
                        pictureUrl = profilePicUrl
                    )
                }
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
}

private fun getCurrentTab(currentDestination: NavDestination?): TasksDestinations {
    val destinations = TasksDestinations.values()
    destinations.forEach { item ->
        val isSelected = currentDestination?.hierarchy?.any {
            it.route == item.route
        } ?: false
        if (isSelected) return item
    }
    return destinations.first()
}
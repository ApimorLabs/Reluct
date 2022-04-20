package work.racka.reluct.android.compose.navigation.navhost.graphs.tasks

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import timber.log.Timber
import work.racka.reluct.android.compose.components.tab.tasks.TasksTabBar
import work.racka.reluct.android.compose.components.textfields.search.ReluctSearchBar
import work.racka.reluct.android.compose.components.topBar.CollapsingToolbarBase
import work.racka.reluct.android.compose.components.topBar.ProfilePicture
import work.racka.reluct.android.screens.tasks.pending.PendingTasksScreen
import work.racka.reluct.common.compose.destinations.TasksDestinations
import work.racka.reluct.common.compose.destinations.navbar.Graphs

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
internal fun TasksNavHost(
    navController: NavHostController = rememberAnimatedNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val tabPage by remember(currentDestination) {
        derivedStateOf {
            getCurrentTab(currentDestination)
        }
    }

    // CollapsingToolbar Implementation
    val toolbarCollapsed = rememberSaveable { mutableStateOf(false) }
    val toolbarOffsetHeightPx = rememberSaveable { mutableStateOf(0f) }

    Scaffold(
        topBar = {
            TasksScreenTopBar(
                tabPage = tabPage,
                profilePicUrl = null,
                toolbarOffset = toolbarOffsetHeightPx.value,
                toolbarCollapsed = toolbarCollapsed.value,
                onCollapsed = {
                    toolbarCollapsed.value = it
                },
                updateTabPage = {
                    navController.navigate(it.route)
                },
            )
        }
    ) {
        AnimatedNavHost(
            navController = navController,
            route = Graphs.RootDestinations.route + Graphs.TasksDestinations.route,
            startDestination = TasksDestinations.Tasks.route
        ) {
            Timber.d("Tasks screen called")

            // Tasks
            composable(
                route = TasksDestinations.Tasks.route
            ) {
                PendingTasksScreen(
                    onNavigateToAddTask = {

                    },
                    onNavigateToTaskDetails = {

                    },
                    updateTopBar = { toolbarOffset ->
                        toolbarOffsetHeightPx.value = toolbarOffset
                    }
                )
            }

            // Done
            composable(
                route = TasksDestinations.Done.route
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tasks: ",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            // Statistics
            composable(
                route = TasksDestinations.Statistics.route
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tasks: ",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TasksScreenTopBar(
    tabPage: TasksDestinations,
    profilePicUrl: String?,
    toolbarOffset: Float,
    toolbarCollapsed: Boolean,
    onCollapsed: (Boolean) -> Unit,
    updateTabPage: (TasksDestinations) -> Unit,
) {
    CollapsingToolbarBase(
        modifier = Modifier
            .statusBarsPadding(),
        toolbarHeading = null,
        toolbarHeight = 120.dp,
        toolbarOffset = toolbarOffset,
        showBackButton = false,
        minShrinkHeight = 60.dp,
        onCollapsed = {
            onCollapsed(it)
        }
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth(),
            verticalArrangement = Arrangement
                .spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                visible = !toolbarCollapsed,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ReluctSearchBar(
                    extraButton = {
                        ProfilePicture(
                            modifier = Modifier,//.padding(4.dp),
                            pictureUrl = profilePicUrl
                        )
                    }
                )
            }
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
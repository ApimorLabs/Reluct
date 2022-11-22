package work.racka.reluct.android.navigation.navhost.graphs.goals

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable
import work.racka.reluct.android.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.navigation.util.NavHelpers
import work.racka.reluct.android.navigation.util.NavHelpers.popBackStackOrExitActivity
import work.racka.reluct.android.screens.goals.addEdit.AddEditGoalScreen
import work.racka.reluct.android.screens.goals.details.GoalDetailsScreen
import work.racka.reluct.common.core.navigation.composeDestinations.goals.AddEditGoalArgs
import work.racka.reluct.common.core.navigation.composeDestinations.goals.AddEditGoalDestination
import work.racka.reluct.common.core.navigation.composeDestinations.goals.GoalDetailsArgs
import work.racka.reluct.common.core.navigation.composeDestinations.goals.GoalDetailsDestination
import work.racka.reluct.compose.common.components.util.BarsVisibility

@ExperimentalAnimationApi
fun NavGraphBuilder.goalDataNavGraph(
    navController: NavHostController,
    barsVisibility: BarsVisibility
) {
    navigation(
        route = work.racka.reluct.common.core.navigation.composeDestinations.goals.GoalDetailsDestination.destination,
        startDestination = work.racka.reluct.common.core.navigation.composeDestinations.goals.GoalDetailsDestination.route
    ) {
        // Goal Details
        composable(
            route = work.racka.reluct.common.core.navigation.composeDestinations.goals.GoalDetailsDestination.route,
            arguments = work.racka.reluct.common.core.navigation.composeDestinations.goals.GoalDetailsDestination.args,
            deepLinks = work.racka.reluct.common.core.navigation.composeDestinations.goals.GoalDetailsDestination.deepLinks,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { backStackEntry ->
            // Its safe to cast since this composable will always be inside an activity
            val activity = LocalContext.current as Activity

            GoalDetailsScreen(
                goalId = NavHelpers.getStringArgs(backStackEntry, work.racka.reluct.common.core.navigation.composeDestinations.goals.GoalDetailsArgs.GoalId.name),
                onExit = { navController.popBackStackOrExitActivity(activity) },
                onNavigateToEditGoal = { goalId ->
                    navController.navigate(work.racka.reluct.common.core.navigation.composeDestinations.goals.AddEditGoalDestination.argsRoute(goalId, null))
                }
            )

            SideEffect { barsVisibility.bottomBar.hide() }
        }

        // Add or Edit Goal
        composable(
            route = work.racka.reluct.common.core.navigation.composeDestinations.goals.AddEditGoalDestination.route,
            arguments = work.racka.reluct.common.core.navigation.composeDestinations.goals.AddEditGoalDestination.args,
            deepLinks = work.racka.reluct.common.core.navigation.composeDestinations.goals.AddEditGoalDestination.deepLinks,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { backStackEntry ->
            // Its safe to cast since this composable will always be inside an activity
            val activity = LocalContext.current as Activity

            val goalIndex = backStackEntry.arguments
                ?.getInt(work.racka.reluct.common.core.navigation.composeDestinations.goals.AddEditGoalArgs.DefaultGoalIndex.name)

            AddEditGoalScreen(
                goalId = NavHelpers.getStringArgs(backStackEntry, work.racka.reluct.common.core.navigation.composeDestinations.goals.AddEditGoalArgs.GoalId.name),
                defaultGoalIndex = goalIndex?.let { i -> if (i < 0) null else i },
                onExit = { navController.popBackStackOrExitActivity(activity) }
            )

            SideEffect { barsVisibility.bottomBar.hide() }
        }
    }
}

package work.racka.reluct.android.compose.navigation.navhost.graphs.goals

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.navigation.transitions.scaleInEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleInPopEnterTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutExitTransition
import work.racka.reluct.android.compose.navigation.transitions.scaleOutPopExitTransition
import work.racka.reluct.android.compose.navigation.util.NavHelpers
import work.racka.reluct.android.compose.navigation.util.NavHelpers.popBackStackOrExitActivity
import work.racka.reluct.android.screens.goals.add_edit.AddEditGoalScreen
import work.racka.reluct.common.core_navigation.compose_destinations.goals.AddEditGoalArgs
import work.racka.reluct.common.core_navigation.compose_destinations.goals.AddEditGoalDestination
import work.racka.reluct.common.core_navigation.compose_destinations.goals.GoalDetailsArgs
import work.racka.reluct.common.core_navigation.compose_destinations.goals.GoalDetailsDestination

@ExperimentalAnimationApi
fun NavGraphBuilder.goalDataNavGraph(
    navController: NavHostController,
    barsVisibility: BarsVisibility
) {
    navigation(
        route = GoalDetailsDestination.destination,
        startDestination = GoalDetailsDestination.route
    ) {
        // Goal Details
        composable(
            route = GoalDetailsDestination.route,
            arguments = GoalDetailsDestination.args,
            deepLinks = GoalDetailsDestination.deepLinks,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { backStackEntry ->
            barsVisibility.bottomBar.hide()

            // Its safe to cast since this composable will always be inside an activity
            val activity = LocalContext.current as Activity

            val goalId = NavHelpers.getStringArgs(backStackEntry, GoalDetailsArgs.GoalId.name)

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Goal Id is: $goalId")
            }
        }

        // Add or Edit Goal
        composable(
            route = AddEditGoalDestination.route,
            arguments = AddEditGoalDestination.args,
            deepLinks = AddEditGoalDestination.deepLinks,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() }
        ) { backStackEntry ->
            barsVisibility.bottomBar.hide()

            // Its safe to cast since this composable will always be inside an activity
            val activity = LocalContext.current as Activity

            val goalIndex = backStackEntry.arguments
                ?.getInt(AddEditGoalArgs.DefaultGoalIndex.name)

            AddEditGoalScreen(
                goalId = NavHelpers.getStringArgs(backStackEntry, AddEditGoalArgs.GoalId.name),
                defaultGoalIndex = goalIndex?.let { i -> if (i < 0) null else i },
                onExit = { navController.popBackStackOrExitActivity(activity) }
            )
        }
    }
}
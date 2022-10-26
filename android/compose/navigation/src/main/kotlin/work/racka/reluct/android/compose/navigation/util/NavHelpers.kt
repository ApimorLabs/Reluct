package work.racka.reluct.android.compose.navigation.util

import android.app.Activity
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

internal object NavHelpers {
    fun getStringArgs(backStackEntry: NavBackStackEntry, key: String?): String? {
        val arg = backStackEntry.arguments?.getString(key)
        return if (arg == "null") null else arg
    }

    /** Useful when destination is launched from a deeplink and no backstack is present **/
    fun NavHostController.popBackStackOrExitActivity(activity: Activity) {
        if (!popBackStack()) {
            activity.finish()
        }
    }

    /**
     * Navigates Nav bar elements that can be the Top or Bottom Navbar while making sure only one
     * destination instance is on the stack and pop unwanted destinations such that it always
     * returns to start destination when you pop the current destination
     */
    fun NavHostController.navigateNavBarElements(route: String) {
        navigate(route = route) {
            popUpTo(graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
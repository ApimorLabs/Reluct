package work.racka.reluct.android.compose.navigation.util

import android.app.Activity
import androidx.navigation.NavBackStackEntry
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
}
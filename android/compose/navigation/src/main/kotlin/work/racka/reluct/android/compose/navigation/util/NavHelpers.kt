package work.racka.reluct.android.compose.navigation.util

import androidx.navigation.NavBackStackEntry

internal object NavHelpers {
    fun getStringArgs(backStackEntry: NavBackStackEntry, key: String?): String? {
        val arg = backStackEntry.arguments?.getString(key)
        return if (arg == "null") null else arg
    }
}
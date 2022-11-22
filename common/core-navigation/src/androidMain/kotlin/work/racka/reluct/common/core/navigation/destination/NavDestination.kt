package work.racka.reluct.common.core.navigation.destination

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

/**
 * Interface for describing the app's Android navigation destinations
 * Backed by androidx.navigation
 */

interface NavDestination {
    /**
     * Defines a specific route this destination belongs to.
     * Route is a String that defines the path to your composable.
     * You can think of it as an implicit deep link that leads to a specific destinatio
     * n.
     * Each destination should have a unique route.
     */
    val route: String

    /**
     * Defines a specific destination ID.
     * This is needed when using nested graphs via the navigation DLS, to differentiate a specific
     * destination's route from the route of the entire nested graph it belongs to.
     */
    val destination: String

    /**
     * Nav Arguments that are to be used in the composable of this destination
     */
    val args: List<NamedNavArgument>
        get() = listOf()

    /**
     * Nav Deep Links that are to be used in the composable of this destination
     */
    val deepLinks: List<NavDeepLink>
        get() = listOf()
}

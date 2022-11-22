package work.racka.reluct.android.navigation.toptabs.dashboard

import work.racka.reluct.common.core.navigation.composeDestinations.dashboard.DashboardOverviewDestination
import work.racka.reluct.common.core.navigation.composeDestinations.dashboard.DashboardStatsDestination

enum class DashboardTabDestination(
    val route: String
) {
    Overview(
        route = work.racka.reluct.common.core.navigation.composeDestinations.dashboard.DashboardOverviewDestination.route
    ),
    Statistics(
        route = work.racka.reluct.common.core.navigation.composeDestinations.dashboard.DashboardStatsDestination.route
    );
}

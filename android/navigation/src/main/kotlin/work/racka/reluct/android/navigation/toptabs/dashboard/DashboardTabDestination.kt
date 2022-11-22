package work.racka.reluct.android.navigation.toptabs.dashboard

import work.racka.reluct.common.core.navigation.composeDestinations.dashboard.DashboardOverviewDestination
import work.racka.reluct.common.core.navigation.composeDestinations.dashboard.DashboardStatsDestination

enum class DashboardTabDestination(val route: String) {
    Overview(route = DashboardOverviewDestination.route),
    Statistics(route = DashboardStatsDestination.route);
}

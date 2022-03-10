package work.racka.reluct.compose.navigation.destinations

internal enum class Graphs(
    val route: String
) {
    RootDestinations(
        route = "RootDestination"
    ),
    DashboardDestinations(
        route = NavbarDestinations.Dashboard.name
    ),
    TasksDestinations(
        route = NavbarDestinations.Tasks.name
    ),
    ScreenTimeDestinations(
        route = NavbarDestinations.ScreenTime.name
    ),
    GoalsDestinations(
        route = NavbarDestinations.Goals.name
    );
}
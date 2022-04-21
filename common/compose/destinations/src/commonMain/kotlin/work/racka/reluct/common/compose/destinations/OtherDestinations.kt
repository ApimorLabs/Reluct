package work.racka.reluct.common.compose.destinations

enum class OtherDestinations(
    val route: String,
) {
    AddEditTask(
        route = "OtherDestinations-AddEditTask"
    ),
    TaskDetails(
        route = "OtherDestinations-TaskDetails"
    );
}
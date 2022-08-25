package work.racka.reluct.common.features.dashboard.overview.states

sealed class DashboardEvents {
    object Nothing : DashboardEvents()
    data class ShowMessageDone(val isDone: Boolean, val msg: String) : DashboardEvents()
}

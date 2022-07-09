package work.racka.reluct.common.features.screen_time.limits.states

sealed class ScreenTimeLimitsEvents {
    object Nothing : ScreenTimeLimitsEvents()
    data class ShowMessage(val msg: String) : ScreenTimeLimitsEvents()
    data class DisplayErrorMsg(
        val msg: String,
    ) : ScreenTimeLimitsEvents()

    sealed class Navigation : ScreenTimeLimitsEvents() {
        data class NavigateToAppInfo(
            val packageName: String
        ) : Navigation()

        data class OpenAppTimerSettings(
            val packageName: String
        ) : Navigation()

        object GoBack : Navigation()
    }
}

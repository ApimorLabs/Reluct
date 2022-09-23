package work.racka.reluct.common.features.onboarding.states

import work.racka.reluct.common.settings.Keys

data class OnBoardingState(
    val currentPage: OnBoardingPages = OnBoardingPages.Welcome,
    val permissionsState: PermissionsState = PermissionsState(),
    val currentThemeValue: Int = Keys.Defaults.THEME
)

sealed class OnBoardingPages {
    object Welcome : OnBoardingPages()
    object Permissions : OnBoardingPages()
    object Notifications : OnBoardingPages()
    object Reminders : OnBoardingPages()
    object UsageAccess : OnBoardingPages()
    object Overlay : OnBoardingPages()
    object Themes : OnBoardingPages()
    object AllSet : OnBoardingPages()
}

data class PermissionsState(
    val notificationGranted: Boolean = false,
    val alarmsAndRemindersGranted: Boolean = false,
    val usageAccessGranted: Boolean = false,
    val overlayGranted: Boolean = false
)

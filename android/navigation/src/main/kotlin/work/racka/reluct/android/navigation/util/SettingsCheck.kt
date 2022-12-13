package work.racka.reluct.android.navigation.util

data class SettingsCheck(
    val isOnBoardingDone: Boolean,
    val showChangeLog: Boolean,
    val accountCheck: AccountCheck?,
    val loginSkipped: Boolean
)

data class AccountCheck(
    val isEmailVerified: Boolean,
    val email: String
)

package work.racka.reluct.common.features.settings.states

sealed class UserAccountEvents {
    object None : UserAccountEvents()
    object RefreshFailed : UserAccountEvents()
    class PasswordReset(val success: Boolean) : UserAccountEvents()
}

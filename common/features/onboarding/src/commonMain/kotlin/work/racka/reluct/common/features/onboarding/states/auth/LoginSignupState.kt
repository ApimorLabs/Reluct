package work.racka.reluct.common.features.onboarding.states.auth

import work.racka.reluct.common.model.domain.authentication.*

data class LoginSignupState(
    val authState: CurrentAuthState = CurrentAuthState.None,
    val credVerificationState: CredVerificationState = CredVerificationState(),
    val screenLoading: Boolean = false
)

sealed class CurrentAuthState {
    data class Login(
        val user: EmailUserLogin = EmailUserLogin(email = "", password = "")
    ) : CurrentAuthState()

    data class Signup(
        val user: RegisterUser = RegisterUser(
            displayName = "",
            profilePicUrl = null,
            email = "",
            password = "",
            repeatPassword = ""
        ),
        val correctRepeatPassword: Boolean = true
    ) : CurrentAuthState()

    data class Authenticated(val user: User) : CurrentAuthState()

    object None : CurrentAuthState()
}

data class CredVerificationState(
    val email: EmailResult = EmailResult.VALID,
    val password: PasswordResult = PasswordResult.VALID,
    val canLoginOrSignup: Boolean = false
)

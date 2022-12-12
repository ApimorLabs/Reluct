package work.racka.reluct.common.features.onboarding.states.auth

sealed class LoginSignupEvent {
    class LoginError(val attempts: Int, val message: String) : LoginSignupEvent()
    class SignupError(val email: String, val message: String) : LoginSignupEvent()
    class Error(val message: String) : LoginSignupEvent()
}

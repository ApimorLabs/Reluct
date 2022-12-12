package work.racka.reluct.common.features.onboarding.vm

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.domain.usecases.authentication.AuthVerifications
import work.racka.reluct.common.domain.usecases.authentication.LoginSignupUser
import work.racka.reluct.common.features.onboarding.states.auth.CredVerificationState
import work.racka.reluct.common.features.onboarding.states.auth.CurrentAuthState
import work.racka.reluct.common.features.onboarding.states.auth.LoginSignupEvent
import work.racka.reluct.common.features.onboarding.states.auth.LoginSignupState
import work.racka.reluct.common.model.domain.authentication.EmailResult
import work.racka.reluct.common.model.domain.authentication.EmailUserLogin
import work.racka.reluct.common.model.domain.authentication.PasswordResult
import work.racka.reluct.common.model.domain.authentication.RegisterUser
import work.racka.reluct.common.model.util.Resource

class LoginSignupViewModel(
    private val auth: LoginSignupUser,
    private val verifications: AuthVerifications
) : CommonViewModel() {

    private val authState: MutableStateFlow<CurrentAuthState> =
        MutableStateFlow(CurrentAuthState.Login())
    private val credVerificationState = MutableStateFlow(CredVerificationState())

    val uiState: StateFlow<LoginSignupState> = combine(
        authState, credVerificationState
    ) { authState, credVerificationState ->
        LoginSignupState(
            authState = authState,
            credVerificationState = credVerificationState
        )
    }.stateIn(
        scope = vmScope,
        initialValue = LoginSignupState(),
        started = SharingStarted.WhileSubscribed(5_000L)
    )

    private val eventsChannel = Channel<LoginSignupEvent>(Channel.UNLIMITED)
    val events: Flow<LoginSignupEvent> = eventsChannel.receiveAsFlow()

    private var loginAttempts = 0

    fun updateLoginUser(user: EmailUserLogin) {
        authState.update { CurrentAuthState.Login(user) }
        checkCanLogin(user)
    }

    fun updateRegisterUser(user: RegisterUser) {
        authState.update {
            CurrentAuthState.Signup(
                user = user,
                correctRepeatPassword = user.password == user.repeatPassword
            )
        }
        checkCanSignup(user)
    }

    fun openLogin() {
        authState.update { CurrentAuthState.Login() }
        credVerificationState.update { CredVerificationState() }
    }

    fun openSignup() {
        authState.update { CurrentAuthState.Signup() }
        credVerificationState.update { CredVerificationState() }
    }

    fun login() {
        vmScope.launch {
            val currentAuthState = authState.value
            if (currentAuthState is CurrentAuthState.Login) {
                loginAttempts++
                when (val res = auth.emailLogin(currentAuthState.user)) {
                    is Resource.Success -> authState.update {
                        CurrentAuthState.Authenticated(res.data)
                    }
                    is Resource.Error -> eventsChannel.send(
                        LoginSignupEvent.LoginError(loginAttempts, res.message)
                    )
                    else -> eventsChannel.send(
                        LoginSignupEvent.Error("Unknown Error! Attempt $loginAttempts")
                    )
                }
            } else {
                eventsChannel.send(
                    LoginSignupEvent.Error("Invalid Application State! Try again!")
                )
            }
        }
    }

    fun signup() {
        vmScope.launch {
            val currentAuthState = authState.value
            if (currentAuthState is CurrentAuthState.Signup) {
                loginAttempts++
                when (val res = auth.emailSignup(currentAuthState.user)) {
                    is Resource.Success -> authState.update {
                        CurrentAuthState.Authenticated(res.data)
                    }
                    is Resource.Error -> eventsChannel.send(
                        LoginSignupEvent.LoginError(loginAttempts, res.message)
                    )
                    else -> eventsChannel.send(
                        LoginSignupEvent.Error("Unknown Error! Attempt $loginAttempts")
                    )
                }
            } else {
                eventsChannel.send(
                    LoginSignupEvent.Error("Invalid Application State! Try again!")
                )
            }
        }
    }

    private fun checkCanLogin(user: EmailUserLogin) {
        val emailResult = verifications.validateEmail(user.email)
        val passwordResult = verifications.validatePassword(user.password)
        credVerificationState.update {
            CredVerificationState(
                email = emailResult,
                password = passwordResult,
                canLoginOrSignup = emailResult == EmailResult.VALID &&
                        passwordResult == PasswordResult.VALID
            )
        }
    }

    private fun checkCanSignup(user: RegisterUser) {
        val emailResult = verifications.validateEmail(user.email)
        val passwordResult = verifications.validatePassword(user.password)
        credVerificationState.update {
            CredVerificationState(
                email = emailResult,
                password = passwordResult,
                canLoginOrSignup = emailResult == EmailResult.VALID &&
                        passwordResult == PasswordResult.VALID &&
                        user.password == user.repeatPassword
            )
        }
    }
}

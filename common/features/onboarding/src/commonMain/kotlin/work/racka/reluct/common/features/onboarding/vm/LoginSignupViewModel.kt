package work.racka.reluct.common.features.onboarding.vm

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.domain.usecases.authentication.AuthVerifications
import work.racka.reluct.common.domain.usecases.authentication.LoginSignupUser
import work.racka.reluct.common.domain.usecases.authentication.UserAccountManagement
import work.racka.reluct.common.features.onboarding.states.auth.CredVerificationState
import work.racka.reluct.common.features.onboarding.states.auth.CurrentAuthState
import work.racka.reluct.common.features.onboarding.states.auth.LoginSignupEvent
import work.racka.reluct.common.features.onboarding.states.auth.LoginSignupState
import work.racka.reluct.common.model.domain.authentication.*
import work.racka.reluct.common.model.util.Resource
import work.racka.reluct.common.settings.MultiplatformSettings

class LoginSignupViewModel(
    private val auth: LoginSignupUser,
    private val manageUser: UserAccountManagement,
    private val verifications: AuthVerifications,
    private val settings: MultiplatformSettings
) : CommonViewModel() {

    private val authState: MutableStateFlow<CurrentAuthState> =
        MutableStateFlow(CurrentAuthState.None)
    private val credVerificationState = MutableStateFlow(CredVerificationState())
    private val screenLoading = MutableStateFlow(false)

    val uiState: StateFlow<LoginSignupState> = combine(
        authState,
        credVerificationState,
        screenLoading
    ) { authState, credVerificationState, screenLoading ->
        LoginSignupState(
            authState = authState,
            credVerificationState = credVerificationState,
            screenLoading = screenLoading
        )
    }.stateIn(
        scope = vmScope,
        initialValue = LoginSignupState(),
        started = SharingStarted.WhileSubscribed(5_000L)
    )

    private val eventsChannel = Channel<LoginSignupEvent>(Channel.UNLIMITED)
    val events: Flow<LoginSignupEvent> = eventsChannel.receiveAsFlow()

    private var loginAttempts = 0

    init {
        initialize()
    }

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
            if (currentAuthState is CurrentAuthState.Login && loginAttempts <= 5) {
                screenLoading.update { true }
                loginAttempts++
                when (val res = auth.emailLogin(currentAuthState.user)) {
                    is Resource.Success -> {
                        if (res.data.isEmailVerified) {
                            eventsChannel.send(LoginSignupEvent.Continue)
                        } else {
                            auth.sendVerificationEmail(res.data)
                            authState.update { CurrentAuthState.Authenticated(res.data) }
                        }
                    }
                    is Resource.Error -> eventsChannel.send(
                        LoginSignupEvent.LoginError(loginAttempts, res.message)
                    )
                    else -> eventsChannel.send(
                        LoginSignupEvent.Error("Unknown Error! Attempt $loginAttempts")
                    )
                }
                screenLoading.update { false }
            } else if (loginAttempts > 5) {
                eventsChannel.send(
                    LoginSignupEvent.LoginError(loginAttempts, "Too Many Login Attempts!")
                )
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
                screenLoading.update { true }
                when (val res = auth.emailSignup(currentAuthState.user)) {
                    is Resource.Success -> {
                        if (res.data.isEmailVerified) {
                            eventsChannel.send(LoginSignupEvent.Continue)
                        } else {
                            auth.sendVerificationEmail(res.data)
                            authState.update { CurrentAuthState.Authenticated(res.data) }
                        }
                    }
                    is Resource.Error -> eventsChannel.send(
                        LoginSignupEvent.SignupError(
                            email = currentAuthState.user.email,
                            message = res.message
                        )
                    )
                    else -> eventsChannel.send(LoginSignupEvent.Error("Unknown Error! Try again!"))
                }
                screenLoading.update { false }
            } else {
                eventsChannel.send(
                    LoginSignupEvent.Error("Invalid Application State! Try again!")
                )
            }
        }
    }

    fun refreshUser() {
        vmScope.launch {
            screenLoading.update { true }
            when (val res = manageUser.refreshUser()) {
                is Resource.Success -> authState.update { CurrentAuthState.Authenticated(res.data) }
                is Resource.Error -> eventsChannel.send(LoginSignupEvent.Error(res.message))
                else -> eventsChannel.send(LoginSignupEvent.Error("Refresh Error!"))
            }
            screenLoading.update { false }
        }
    }

    fun resendVerificationEmail(user: User) {
        vmScope.launch {
            screenLoading.update { true }
            auth.sendVerificationEmail(user)
            screenLoading.update { false }
        }
    }

    fun markLoginSkipped() {
        settings.saveLoginSkipped(true)
    }

    fun logout() {
        manageUser.logout()
        openLogin()
    }

    private fun initialize() {
        when (val user = manageUser.getUser()) {
            null -> authState.update { CurrentAuthState.Login() }
            else -> authState.update { CurrentAuthState.Authenticated(user) }
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
                    user.password == user.repeatPassword &&
                    user.displayName.isNotBlank()
            )
        }
    }
}

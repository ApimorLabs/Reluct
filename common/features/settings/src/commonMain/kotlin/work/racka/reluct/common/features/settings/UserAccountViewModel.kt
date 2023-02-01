package work.racka.reluct.common.features.settings

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.domain.usecases.authentication.UserAccountManagement
import work.racka.reluct.common.features.settings.states.UserAccountEvents
import work.racka.reluct.common.features.settings.states.UserAccountState
import work.racka.reluct.common.model.domain.authentication.User
import work.racka.reluct.common.model.util.Resource

class UserAccountViewModel(
    private val manageUser: UserAccountManagement
) : CommonViewModel() {

    private val _uiState = MutableStateFlow<UserAccountState>(UserAccountState.Updating)
    val uiState = _uiState.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = UserAccountState.Updating
    )

    private val eventsChannel = Channel<UserAccountEvents>(Channel.UNLIMITED)
    val events: Flow<UserAccountEvents> = eventsChannel.receiveAsFlow()

    private var currentUser: User? = null

    init {
        initialize()
    }

    private fun initialize() {
        _uiState.update { UserAccountState.Updating }
        manageUser.getUser()?.let { user -> updateUser(user) }
            ?: run { _uiState.update { UserAccountState.NotSignedIn } }
    }

    fun refresh() {
        vmScope.launch {
            _uiState.update { UserAccountState.Updating }
            resetEvents()
            when (val res = manageUser.refreshUser()) {
                is Resource.Success -> updateUser(res.data)
                else -> {
                    eventsChannel.send(UserAccountEvents.RefreshFailed)
                    checkCurrentUser()
                }
            }
        }
    }

    fun logout() {
        vmScope.launch {
            _uiState.update { UserAccountState.Updating }
            manageUser.logout()
            _uiState.update { UserAccountState.NotSignedIn }
        }
    }

    fun requestPasswordReset() {
        vmScope.launch {
            val state = _uiState.value
            if (state is UserAccountState.Account) {
                _uiState.update { state.copy(isUpdating = true) }
                resetEvents()
                when (manageUser.requestPasswordResetEmail(state.user.email)) {
                    is Resource.Success -> eventsChannel.send(UserAccountEvents.PasswordReset(true))
                    is Resource.Error -> eventsChannel.send(UserAccountEvents.PasswordReset(false))
                    else -> {}
                }
                _uiState.update { state.copy(isUpdating = false) }
                logout()
            } else {
                eventsChannel.send(UserAccountEvents.PasswordReset(false))
            }
        }
    }

    private suspend fun resetEvents() {
        eventsChannel.send(UserAccountEvents.None)
    }

    private fun updateUser(user: User) {
        currentUser = user
        _uiState.update { UserAccountState.Account(user) }
    }

    private fun checkCurrentUser() {
        currentUser?.let { user -> _uiState.update { UserAccountState.Account(user) } }
            ?: run { _uiState.update { UserAccountState.NotSignedIn } }
    }
}

package work.racka.reluct.android.screens.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.common.features.onboarding.states.auth.LoginSignupEvent
import work.racka.reluct.common.features.onboarding.vm.LoginSignupViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AuthenticationScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = getCommonViewModel<LoginSignupViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = LoginSignupEvent.None)

    val snackbarState = remember { SnackbarHostState() }
    HandleEvents(eventsState = events, snackbarState = snackbarState, onContinue = onContinue)

    AuthenticationScreenUI(
        uiState = uiState,
        snackbarState = snackbarState,
        onChooseAuth = { type ->
            when (type) {
                AuthType.LOGIN -> run(viewModel::openLogin)
                AuthType.SIGNUP -> run(viewModel::openSignup)
                else -> {}
            }
        },
        onUpdateUser = { update ->
            when (update) {
                is UpdateUser.EmailLogin -> update.user.run(viewModel::updateLoginUser)
                is UpdateUser.EmailRegister -> update.user.run(viewModel::updateRegisterUser)
            }
        },
        onAuthAction = { type ->
            when (type) {
                AuthType.LOGIN -> run(viewModel::login)
                AuthType.SIGNUP -> run(viewModel::signup)
                AuthType.LOGOUT -> run(viewModel::logout)
            }
        },
        onRefreshUser = viewModel::refreshUser,
        onResendEmail = viewModel::resendVerificationEmail,
        onContinue = onContinue,
        onMarkSkipped = viewModel::markLoginSkipped,
        modifier = modifier
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<LoginSignupEvent>,
    snackbarState: SnackbarHostState,
    onContinue: () -> Unit
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is LoginSignupEvent.LoginError -> launch { snackbarState.showSnackbar(events.message) }
            is LoginSignupEvent.SignupError -> launch { snackbarState.showSnackbar(events.message) }
            is LoginSignupEvent.Error -> launch { snackbarState.showSnackbar(events.message) }
            is LoginSignupEvent.Continue -> onContinue()
            is LoginSignupEvent.None -> {}
        }
    }
}

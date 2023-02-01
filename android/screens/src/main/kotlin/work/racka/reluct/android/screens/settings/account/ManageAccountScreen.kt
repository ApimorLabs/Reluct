package work.racka.reluct.android.screens.settings.account

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.settings.UserAccountViewModel
import work.racka.reluct.common.features.settings.states.UserAccountEvents

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ManageAccountScreen(
    onGoBack: () -> Unit,
    onNavigateToAuth: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: UserAccountViewModel = getCommonViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = UserAccountEvents.None)

    HandleEvents(eventsState = events, snackbarHostState = snackbarHostState)

    ManageAccountUI(
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        onRefreshUser = viewModel::refresh,
        onRequestPasswordReset = viewModel::requestPasswordReset,
        onLogout = viewModel::logout,
        onLogin = onNavigateToAuth,
        onBackClicked = onGoBack
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<UserAccountEvents>,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is UserAccountEvents.RefreshFailed -> {
                launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.refresh_acc_failed),
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is UserAccountEvents.PasswordReset -> {
                val msg = if (events.success) {
                    context.getString(R.string.password_reset_success)
                } else context.getString(R.string.password_reset_failed)

                launch {
                    snackbarHostState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is UserAccountEvents.None -> {}
        }
    }
}

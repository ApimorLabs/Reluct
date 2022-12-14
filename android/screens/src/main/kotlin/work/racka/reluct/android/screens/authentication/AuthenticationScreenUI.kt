package work.racka.reluct.android.screens.authentication

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.screens.authentication.pages.LoginPage
import work.racka.reluct.android.screens.authentication.pages.SignupPage
import work.racka.reluct.android.screens.authentication.pages.VerifyEmailPage
import work.racka.reluct.common.features.onboarding.states.auth.CurrentAuthState
import work.racka.reluct.common.features.onboarding.states.auth.LoginSignupState
import work.racka.reluct.common.model.domain.authentication.EmailUserLogin
import work.racka.reluct.common.model.domain.authentication.RegisterUser
import work.racka.reluct.common.model.domain.authentication.User
import work.racka.reluct.compose.common.components.dialogs.FullScreenLoading
import work.racka.reluct.compose.common.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
internal fun AuthenticationScreenUI(
    uiState: State<LoginSignupState>,
    snackbarState: SnackbarHostState,
    onChooseAuth: (AuthType) -> Unit,
    onUpdateUser: (UpdateUser) -> Unit,
    onLoginOrSignup: (AuthType) -> Unit,
    onRefreshUser: () -> Unit,
    onResendEmail: (User) -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {

    val authState = remember { derivedStateOf { uiState.value.authState } }
    val screenTransition = updateTransition(
        targetState = authState.value,
        label = "AuthScreenTransition"
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
                    shape = RoundedCornerShape(10.dp),
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->

        Box(
            modifier = Modifier
                .animateContentSize()
                .padding(padding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            screenTransition.AnimatedContent(
                contentKey = { state -> state::class }
            ) { state ->
                when (state) {
                    is CurrentAuthState.Login -> {
                        LoginPage(
                            state = state,
                            verificationState = uiState.value.credVerificationState,
                            isLoading = uiState.value.screenLoading,
                            onUpdateUser = { onUpdateUser(UpdateUser.EmailLogin(it)) },
                            onLogin = { onLoginOrSignup(AuthType.LOGIN) },
                            onOpenSignup = { onChooseAuth(AuthType.SIGNUP) }
                        )
                    }
                    is CurrentAuthState.Signup -> {
                        SignupPage(
                            state = state,
                            verificationState = uiState.value.credVerificationState,
                            isLoading = uiState.value.screenLoading,
                            onUpdateUser = { onUpdateUser(UpdateUser.EmailRegister(it)) },
                            onSignup = { onLoginOrSignup(AuthType.SIGNUP) },
                            onOpenLogin = { onChooseAuth(AuthType.LOGIN) }
                        )
                    }
                    is CurrentAuthState.Authenticated -> {
                        VerifyEmailPage(
                            userEmail = state.user.email,
                            isEmailVerified = state.user.isEmailVerified,
                            isLoading = uiState.value.screenLoading,
                            onRefresh = onRefreshUser,
                            onResendEmail = { onResendEmail(state.user) },
                            onContinue = onContinue
                        )
                    }
                    is CurrentAuthState.None -> {
                        FullScreenLoading(isLoadingProvider = { true })
                    }
                }
            }
        }
    }
}

internal enum class AuthType {
    LOGIN, SIGNUP;
}

internal sealed class UpdateUser {
    data class EmailLogin(val user: EmailUserLogin) : UpdateUser()
    data class EmailRegister(val user: RegisterUser) : UpdateUser()
}

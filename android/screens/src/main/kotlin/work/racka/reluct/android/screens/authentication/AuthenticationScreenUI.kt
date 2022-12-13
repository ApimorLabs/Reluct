package work.racka.reluct.android.screens.authentication

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import work.racka.reluct.common.features.onboarding.states.auth.CurrentAuthState
import work.racka.reluct.common.features.onboarding.states.auth.LoginSignupState
import work.racka.reluct.common.model.domain.authentication.EmailUserLogin
import work.racka.reluct.common.model.domain.authentication.RegisterUser
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
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {

    val screenTransition = updateTransition(
        targetState = uiState.value.authState,
        label = "AuthScreenTransition"
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
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
                contentKey = { state -> state }
            ) { state ->
                when (state) {
                    is CurrentAuthState.Login -> {
                        // Login
                    }
                    is CurrentAuthState.Signup -> {
                        // Signup
                    }
                    is CurrentAuthState.Authenticated -> {
                        // Authenticated
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

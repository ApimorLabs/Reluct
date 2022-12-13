package work.racka.reluct.android.screens.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import work.racka.reluct.common.features.onboarding.states.auth.LoginSignupState
import work.racka.reluct.common.model.domain.authentication.EmailUserLogin
import work.racka.reluct.common.model.domain.authentication.RegisterUser

@Composable
internal fun AuthenticationScreenUI(
    uiState: State<LoginSignupState>,
    onChooseAuth: (AuthType) -> Unit,
    onUpdateUser: (UpdateUser) -> Unit,
    onLoginOrSignup: (AuthType) -> Unit,
    onRefreshUser: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {

}

internal enum class AuthType {
    LOGIN, SIGNUP;
}

internal sealed class UpdateUser {
    data class EmailLogin(val user: EmailUserLogin) : UpdateUser()
    data class EmailRegister(val user: RegisterUser) : UpdateUser()
}

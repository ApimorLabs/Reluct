package work.racka.reluct.android.screens.authentication.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.onboarding.states.auth.CredVerificationState
import work.racka.reluct.common.features.onboarding.states.auth.CurrentAuthState
import work.racka.reluct.common.model.domain.authentication.EmailResult
import work.racka.reluct.common.model.domain.authentication.EmailUserLogin
import work.racka.reluct.common.model.domain.authentication.PasswordResult
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.buttons.OutlinedReluctButton
import work.racka.reluct.compose.common.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.compose.common.components.resources.painterResource
import work.racka.reluct.compose.common.components.textfields.ReluctTextField
import work.racka.reluct.compose.common.components.util.imePadding
import work.racka.reluct.compose.common.theme.Dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LoginPage(
    state: CurrentAuthState.Login,
    verificationState: CredVerificationState,
    isLoading: Boolean,
    onUpdateUser: (EmailUserLogin) -> Unit,
    onLogin: () -> Unit,
    onOpenSignup: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val drawableSize = 200.dp

    val emailErrorText by remember(verificationState.email) {
        derivedStateOf {
            when (verificationState.email) {
                EmailResult.BLANK -> context.getString(R.string.email_error_blank)
                EmailResult.INVALID -> context.getString(R.string.email_error_invalid)
                else -> ""
            }
        }
    }
    val passwordErrorText by remember(verificationState.password) {
        derivedStateOf {
            when (verificationState.password) {
                PasswordResult.INCORRECT_LENGTH -> context.getString(R.string.password_error_length)
                PasswordResult.DIGITS_LETTERS_ABSENT ->
                    context.getString(R.string.password_error_digits_letter)
                else -> ""
            }
        }
    }
    var showPassword by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.LargePadding.size)
                then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            ListGroupHeadingHeader(
                text = stringResource(id = R.string.login_text),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 40.sp)
            )
        }

        item {
            Image(
                modifier = Modifier
                    .size(drawableSize),
                painter = painterResource(SharedRes.assets.login_art),
                contentDescription = null
            )
        }

        // Email
        item {
            ReluctTextField(
                value = state.user.email,
                hint = stringResource(id = R.string.email_text),
                onTextChange = { onUpdateUser(state.user.copy(email = it)) },
                isError = verificationState.email != EmailResult.VALID,
                errorText = emailErrorText,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Email,
                        contentDescription = stringResource(R.string.email_text)
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
        }

        // Password
        item {
            ReluctTextField(
                value = state.user.password,
                hint = stringResource(id = R.string.enter_password_text),
                onTextChange = { onUpdateUser(state.user.copy(password = it)) },
                isError = verificationState.password != PasswordResult.VALID,
                errorText = passwordErrorText,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Password,
                        contentDescription = stringResource(R.string.password_text)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        if (showPassword) {
                            Icon(
                                imageVector = Icons.Rounded.VisibilityOff,
                                contentDescription = stringResource(R.string.password_text)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Visibility,
                                contentDescription = stringResource(R.string.password_text)
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                visualTransformation = if (showPassword) VisualTransformation.None
                else PasswordVisualTransformation()
            )
        }

        // Don't have acc
        item {
            Text(text = stringResource(R.string.do_not_have_account_text))
        }

        // Login & Signup Buttons
        item {
            ReluctButton(
                enabled = verificationState.canLoginOrSignup,
                buttonText = stringResource(R.string.login_text),
                icon = Icons.Rounded.Login,
                onButtonClicked = {
                    focusManager.clearFocus()
                    onLogin()
                },
                showLoading = isLoading
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            OutlinedReluctButton(
                buttonText = stringResource(R.string.signup_text),
                icon = Icons.Rounded.PersonAdd,
                onButtonClicked = onOpenSignup,
                enabled = !isLoading
            )
        }

        item { Spacer(modifier = Modifier.imePadding()) }
    }
}

package work.racka.reluct.android.screens.settings.account

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.settings.states.UserAccountState
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.dialogs.DiscardPromptDialog
import work.racka.reluct.compose.common.components.dialogs.FullScreenLoading
import work.racka.reluct.compose.common.components.resources.painterResource
import work.racka.reluct.compose.common.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.compose.common.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
internal fun ManageAccountUI(
    snackbarHostState: SnackbarHostState,
    uiState: State<UserAccountState>,
    onRefreshUser: () -> Unit,
    onRequestPasswordReset: () -> Unit,
    onLogout: () -> Unit,
    onLogin: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val passwordResetDialog = remember { mutableStateOf(false) }
    val logoutDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val screenTransition = updateTransition(
        targetState = uiState.value,
        label = "ManageAccountScreenTransition"
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            ReluctSmallTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = stringResource(R.string.manage_acc_text),
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
                    snackbarData = it,
                    shape = RoundedCornerShape(10.dp),
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .animateContentSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            screenTransition.AnimatedContent(
                modifier = Modifier.fillMaxSize(),
                contentKey = { state -> state::class }
            ) { state ->
                when (state) {
                    is UserAccountState.Updating -> {
                        FullScreenLoading(isLoadingProvider = { true })
                    }
                    is UserAccountState.NotSignedIn -> {
                        NotSignedInUI(onLogin = onLogin)
                    }
                    is UserAccountState.Account -> {
                        UserAccountUI(
                            accountState = state,
                            onRefreshUser = onRefreshUser,
                            onRequestPasswordReset = { passwordResetDialog.value = true },
                            onLogout = { logoutDialog.value = true }
                        )
                    }
                }
            }
        }
    }

    // Logout Dialog
    DiscardPromptDialog(
        dialogTitleProvider = { context.getString(R.string.are_you_sure_text) },
        dialogTextProvider = { context.getString(R.string.logout_req_desc) },
        openDialog = logoutDialog,
        onClose = { logoutDialog.value = false },
        onPositiveClick = onLogout
    )

    // Password Reset req Dialog
    DiscardPromptDialog(
        dialogTitleProvider = { context.getString(R.string.send_password_reset_text) },
        dialogTextProvider = { context.getString(R.string.password_reset_email_description) },
        openDialog = passwordResetDialog,
        onClose = { passwordResetDialog.value = false },
        onPositiveClick = onRequestPasswordReset
    )
}

@Composable
private fun NotSignedInUI(
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawableSize = 200.dp
    val buttonRatio = .9f // 90%

    LazyColumn(
        modifier = Modifier.fillMaxSize() then modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        contentPadding = PaddingValues(vertical = Dimens.LargePadding.size)
    ) {
        item {
            Image(
                modifier = Modifier
                    .size(drawableSize),
                painter = painterResource(SharedRes.assets.login_art),
                contentDescription = null
            )
        }

        // Description
        item {
            Text(
                text = stringResource(id = R.string.login_reason_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = LocalContentColor.current.copy(.7f),
                modifier = Modifier.fillMaxWidth(buttonRatio)
            )
        }

        // Login Button
        item {
            ReluctButton(
                buttonText = stringResource(R.string.login_text),
                icon = Icons.Rounded.Login,
                onButtonClicked = onLogin,
                modifier = Modifier.fillMaxSize(buttonRatio)
            )
        }
    }
}

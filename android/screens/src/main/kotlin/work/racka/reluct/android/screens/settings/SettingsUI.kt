package work.racka.reluct.android.screens.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.cards.card_with_actions.ReluctDescriptionCard
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.screentime.components.LimitsSwitchCard
import work.racka.reluct.android.screens.settings.components.AppAboutInfo
import work.racka.reluct.android.screens.settings.components.ThemesDialog
import work.racka.reluct.common.features.settings.states.SettingsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsUI(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    uiState: SettingsState,
    saveTheme: (value: Int) -> Unit,
    toggleDnd: (value: Boolean) -> Unit,
    toggleFocusMode: (value: Boolean) -> Unit,
    onBackClicked: () -> Unit
) {

    var openThemeDialog by remember { mutableStateOf(false) }

    val focusModeContainerColor by animateColorAsState(
        targetValue = if (uiState.limitSettings.focusModeOn) Color.Green.copy(alpha = .7f)
        else MaterialTheme.colorScheme.error.copy(alpha = .8f)
    )

    val focusModeContentColor by animateColorAsState(
        targetValue = if (uiState.limitSettings.focusModeOn) Color.Black
        else MaterialTheme.colorScheme.onError
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            ReluctSmallTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = stringResource(R.string.settings_text),
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
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(Dimens.MediumPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
        ) {
            // Theme Settings
            item {
                ReluctDescriptionCard(
                    title = {
                        Text(
                            text = stringResource(id = R.string.themes_text),
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalContentColor.current
                        )
                    },
                    description = {
                        Text(
                            text = stringResource(id = R.string.themes_desc_text),
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = .8f)
                        )
                    },
                    icon = Icons.Rounded.DarkMode,
                    rightActions = {
                        Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = "Open")
                    },
                    onClick = { openThemeDialog = true }
                )
            }

            // Turn On Focus Mode
            item {
                LimitsSwitchCard(
                    title = stringResource(R.string.turn_on_focus),
                    description = stringResource(R.string.turn_on_focus_desc),
                    checked = uiState.limitSettings.focusModeOn,
                    onCheckedChange = { toggleFocusMode(it) },
                    icon = Icons.Rounded.AppBlocking
                )
            }

            // Enable DND
            item {
                LimitsSwitchCard(
                    title = stringResource(R.string.turn_on_dnd),
                    description = stringResource(R.string.turn_on_dnd_desc),
                    checked = uiState.limitSettings.dndOn,
                    onCheckedChange = { toggleDnd(it) },
                    icon = Icons.Rounded.DoNotDisturbOnTotalSilence
                )
            }

            // About Details
            item {
                AppAboutInfo()
            }
        }
    }

    if (openThemeDialog) {
        ThemesDialog(
            onDismiss = { openThemeDialog = false },
            currentTheme = uiState.themeValue,
            onSaveTheme = saveTheme
        )
    }
}
package work.racka.reluct.android.screens.settings

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.settings.AppSettingsViewModel
import work.racka.reluct.common.features.settings.states.SettingsEvents

@Composable
fun SettingsScreen(
    goBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: AppSettingsViewModel = getCommonViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = SettingsEvents.Nothing)

    val context = LocalContext.current
    LaunchedEffect(events) {
        handleEvents(
            events = events,
            context = context,
            snackbarHostState = snackbarHostState,
            scope = this
        )
    }

    SettingsUI(
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        saveTheme = viewModel::saveThemeSettings,
        toggleDnd = viewModel::toggleDnd,
        toggleFocusMode = viewModel::toggleFocusMode,
        onBackClicked = goBack
    )
}

private fun handleEvents(
    events: SettingsEvents,
    context: Context,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    when (events) {
        is SettingsEvents.ThemeChanged -> {
            val msg = context.getString(R.string.themes_changed_text)
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is SettingsEvents.DndChanged -> {
            val msg = if (events.isEnabled) context.getString(R.string.dnd_on_msg)
            else context.getString(R.string.dnd_off_msg)
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is SettingsEvents.FocusModeChanged -> {
            val msg = if (events.isEnabled) context.getString(R.string.focus_mode_on_msg)
            else context.getString(R.string.focus_mode_off_msg)
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        else -> {}
    }
}
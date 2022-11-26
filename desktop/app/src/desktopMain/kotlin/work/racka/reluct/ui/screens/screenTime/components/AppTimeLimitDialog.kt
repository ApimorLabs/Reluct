package work.racka.reluct.ui.screens.screenTime.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import work.racka.reluct.common.features.screenTime.limits.states.AppTimeLimitState
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.dialogs.CircularProgressDialog
import work.racka.reluct.compose.common.components.resources.stringResource

@Composable
internal fun ShowAppTimeLimitDialog(
    openDialog: State<Boolean>,
    limitStateProvider: () -> AppTimeLimitState,
    onSaveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    onClose: () -> Unit,
) {
    when (val limitState = limitStateProvider()) {
        is AppTimeLimitState.Data -> {
            AppTimeLimitDialog(
                openDialog = openDialog,
                onDismiss = onClose,
                initialAppTimeLimit = limitState.timeLimit,
                onSaveTimeLimit = onSaveTimeLimit
            )
        }
        else -> {
            CircularProgressDialog(
                onDismiss = onClose,
                loadingText = stringResource(SharedRes.strings.loading_text),
                isVisible = openDialog
            )
        }
    }
}

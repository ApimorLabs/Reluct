package work.racka.reluct.android.screens.screentime.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.screenTime.limits.states.AppTimeLimitState
import work.racka.reluct.compose.common.components.dialogs.CircularProgressDialog

@Composable
internal fun ShowAppTimeLimitDialog(
    openDialog: State<Boolean>,
    limitStateProvider: () -> AppTimeLimitState,
    onSaveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    onClose: () -> Unit,
) {
    if (openDialog.value) {
        when (val limitState = limitStateProvider()) {
            is AppTimeLimitState.Data -> {
                AppTimeLimitDialog(
                    onDismiss = onClose,
                    initialAppTimeLimit = limitState.timeLimit,
                    onSaveTimeLimit = onSaveTimeLimit
                )
            }
            else -> {
                CircularProgressDialog(
                    onDismiss = onClose,
                    loadingText = stringResource(id = R.string.loading_text),
                    isVisible = openDialog
                )
            }
        }
    }
}

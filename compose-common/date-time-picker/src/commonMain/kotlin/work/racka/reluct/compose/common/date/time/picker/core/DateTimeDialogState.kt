package work.racka.reluct.compose.common.date.time.picker.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusManager

/**
 *  The [DateTimeDialogState] class is used to store the state for a [DateTimeDialog]
 *
 * @param initialValue the initial showing state of the dialog
 */
class DateTimeDialogState(
    initialValue: Boolean = false,
    val dialogProperties: DateTimeDialogProperties = DateTimeDialogProperties(),
    val buttonText: DateTimeDialogButtonText = DateTimeDialogButtonText()
) {
    var showing by mutableStateOf(initialValue)

    /**
     *  Shows the dialog
     */
    fun show() {
        showing = true
    }

    /**
     * Clears focus with a given [FocusManager] and then hides the dialog
     *
     * @param focusManager the focus manager of the dialog view
     */
    fun hide(focusManager: FocusManager? = null) {
        focusManager?.clearFocus()
        showing = false
    }

    companion object {
        /**
         * The default [saver] implementation for [DateTimeDialogState].
         */
        fun saver(
            dialogProperties: DateTimeDialogProperties = DateTimeDialogProperties(),
            buttonText: DateTimeDialogButtonText = DateTimeDialogButtonText()
        ): Saver<DateTimeDialogState, *> = Saver(
            save = { it.showing },
            restore = { DateTimeDialogState(it, dialogProperties, buttonText) }
        )
    }
}

/**
 * Create and [rememberSaveable] a [DateTimeDialogState].
 *
 * @param initialValue the initial showing state of the dialog
 */
@Composable
fun rememberDateTimeDialogState(
    initialValue: Boolean = false,
    dialogProperties: DateTimeDialogProperties = DateTimeDialogProperties(),
    buttonText: DateTimeDialogButtonText = DateTimeDialogButtonText()
): DateTimeDialogState {
    return rememberSaveable(
        dialogProperties,
        buttonText,
        saver = DateTimeDialogState.saver(dialogProperties, buttonText)
    ) {
        DateTimeDialogState(initialValue, dialogProperties, buttonText)
    }
}

package work.racka.reluct.android.compose.components.datetime.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color

/**
 *  The [MaterialDialogState] class is used to store the state for a [MaterialDialog]
 *
 * @param initialValue the initial showing state of the dialog
 */
class MaterialDialogState(initialValue: Boolean = false) {
    var showing by mutableStateOf(initialValue)

    /**
     *  Dialog background color with elevation overlay
     */
    var dialogBackgroundColor by mutableStateOf<Color?>(null)

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
         * The default [Saver] implementation for [MaterialDialogState].
         */
        fun Saver(): Saver<MaterialDialogState, *> = Saver(
            save = { it.showing },
            restore = { MaterialDialogState(it) }
        )
    }
}

/**
 * Create and [remember] a [MaterialDialogState].
 *
 * @param initialValue the initial showing state of the dialog
 */
@Composable
fun rememberMaterialDialogState(initialValue: Boolean = false): MaterialDialogState {
    return rememberSaveable(saver = MaterialDialogState.Saver()) {
        MaterialDialogState(initialValue)
    }
}

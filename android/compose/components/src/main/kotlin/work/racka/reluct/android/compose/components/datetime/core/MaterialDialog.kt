package work.racka.reluct.android.compose.components.datetime.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import work.racka.reluct.android.compose.components.datetime.util.isLargeDevice
import kotlin.math.min

/**
 *  Builds a dialog with the given content
 * @param dialogState state of the dialog
 * @param properties properties of the compose dialog
 * @param backgroundColor background color of the dialog
 * @param shape shape of the dialog and components used in the dialog
 * @param border border stoke of the dialog
 * @param elevation elevation of the dialog
 * @param autoDismiss when true the dialog is hidden on any button press
 * @param onCloseRequest function to be executed when user clicks outside dialog
 * @param buttons the buttons layout of the dialog
 * @param content the body content of the dialog
 */
@Composable
fun MaterialDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    properties: DialogProperties = DialogProperties(),
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    elevation: Dp = 24.dp,
    autoDismiss: Boolean = true,
    onCloseRequest: (MaterialDialogState) -> Unit = { it.hide() },
    buttons: @Composable MaterialDialogButtons.() -> Unit = {},
    content: @Composable MaterialDialogScope.() -> Unit,
) {
    val dialogScope = remember { MaterialDialogScopeImpl(dialogState, autoDismiss) }
    DisposableEffect(dialogState.showing) {
        if (!dialogState.showing) dialogScope.reset()
        onDispose { }
    }

    BoxWithConstraints {
        val maxHeight = if (isLargeDevice()) {
            LocalConfiguration.current.screenHeightDp.dp - 96.dp
        } else {
            560.dp
        }

        val maxHeightPx = with(LocalDensity.current) { maxHeight.toPx().toInt() }
        val isDialogFullWidth = LocalConfiguration.current.screenWidthDp.dp == maxWidth
        val padding = if (isDialogFullWidth) 16.dp else 0.dp

        if (dialogState.showing) {
            dialogState.dialogBackgroundColor = LocalElevationOverlay.current?.apply(
                color = backgroundColor,
                elevation = elevation
            ) ?: MaterialTheme.colorScheme.surface

            Dialog(
                properties = properties,
                onDismissRequest = { onCloseRequest(dialogState) }
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(maxHeight = maxHeight, maxWidth = 560.dp)
                        .padding(horizontal = padding)
                        .clipToBounds()
                        .wrapContentHeight()
                        .testTag("dialog"),
                    shape = shape,
                    color = backgroundColor,
                    border = border,
                    shadowElevation = elevation
                ) {
                    Layout(
                        modifier = Modifier.clip(shape),
                        content = {
                            dialogScope.DialogButtonsLayout(
                                modifier = Modifier.layoutId("buttons"),
                                content = buttons
                            )
                            Column(Modifier.layoutId("content")) { content(dialogScope) }
                        }
                    ) { measurables, constraints ->
                        val buttonsHeight =
                            measurables[0].minIntrinsicHeight(constraints.maxWidth)
                        val buttonsPlaceable = measurables[0].measure(
                            constraints.copy(maxHeight = buttonsHeight, minHeight = 0)
                        )

                        val contentPlaceable = measurables[1].measure(
                            constraints.copy(
                                maxHeight = maxHeightPx - buttonsPlaceable.height,
                                minHeight = 0,
                            )
                        )

                        val height =
                            min(maxHeightPx, buttonsPlaceable.height + contentPlaceable.height)

                        return@Layout layout(constraints.maxWidth, height) {
                            contentPlaceable.place(0, 0)
                            buttonsPlaceable.place(0, height - buttonsPlaceable.height)
                        }
                    }
                }
            }
        }
    }
}
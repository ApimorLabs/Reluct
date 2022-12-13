package work.racka.reluct.compose.common.components.textfields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@Composable
fun ReluctTextField(
    value: String,
    hint: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    errorColor: Color = MaterialTheme.colorScheme.error,
    isError: Boolean = false,
    errorText: String = "Error",
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = Shapes.large,
) {
    val focusRequester = remember { FocusRequester() }

    var isHintActive by remember {
        mutableStateOf(hint.isNotEmpty())
    }
    var isTyping by remember(value) {
        mutableStateOf(value.isNotEmpty())
    }

    val hintAlpha: Float by animateFloatAsState(
        targetValue = if (isHintActive) 1F else 0.5f
    )

    Column(
        verticalArrangement = Arrangement
            .spacedBy(Dimens.ExtraSmallPadding.size)
    ) {
        Row(
            modifier = Modifier
                .clip(shape)
                .background(color = containerColor)
                .border(
                    width = if (isError) 2.dp else 0.dp,
                    color = if (isError) errorColor else Color.Transparent,
                    shape = shape
                )
                .clickable {
                    focusRequester.requestFocus()
                } then modifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalContentColor.provides(contentColor)) {
                leadingIcon?.let {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center,
                        content = { it() }
                    )
                }
            }

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = if (leadingIcon == null) Dimens.MediumPadding.size
                        else Dimens.ExtraSmallPadding.size
                    )
                    .padding(
                        end = if (trailingIcon == null) Dimens.MediumPadding.size
                        else Dimens.ExtraSmallPadding.size
                    )
                    .align(Alignment.Top)
            ) {
                if (!isTyping) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = hint,
                        color = (if (isError) errorColor else contentColor)
                            .copy(alpha = hintAlpha),
                        style = textStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                CompositionLocalProvider(
                    LocalTextSelectionColors provides TextSelectionColors(
                        handleColor = MaterialTheme.colorScheme.primary,
                        backgroundColor = MaterialTheme.colorScheme.primary
                            .copy(alpha = .3f)
                    )
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = {
                            onTextChange(it)
                            isTyping = value.isNotEmpty()
                        },
                        visualTransformation = visualTransformation,
                        maxLines = maxLines,
                        cursorBrush = cursorBrush,
                        singleLine = singleLine,
                        textStyle = textStyle
                            .copy(color = if (isError) errorColor else contentColor),
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.MediumPadding.size)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isHintActive = !it.isFocused
                            }
                    )
                }
            }

            CompositionLocalProvider(LocalContentColor.provides(contentColor)) {
                trailingIcon?.let {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center,
                        content = { it() }
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size),
            visible = isError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = errorText,
                style = textStyle.copy(fontSize = 14.sp),
                color = errorColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

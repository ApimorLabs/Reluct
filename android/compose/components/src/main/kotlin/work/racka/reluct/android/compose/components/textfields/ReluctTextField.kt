package work.racka.reluct.android.compose.components.textfields

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes

@Composable
fun ReluctTextField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    onTextChange: (String) -> Unit,
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

    val isErrorActive = rememberSaveable(isError) {
        mutableStateOf(isError)
    }

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
                    width = if (isErrorActive.value) 2.dp else 0.dp,
                    color = if (isErrorActive.value) errorColor else Color.Transparent,
                    shape = shape
                )
                .clickable {
                    focusRequester.requestFocus()
                } then modifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Spacer(
                modifier = Modifier
                    .width(Dimens.MediumPadding.size)
                    .height(48.dp)
            )
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                if (!isTyping) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = hint,
                        color = (if (isErrorActive.value) errorColor else contentColor)
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

                            if (isErrorActive.value) {
                                isErrorActive.value = false
                            }
                        },
                        visualTransformation = visualTransformation,
                        maxLines = maxLines,
                        cursorBrush = cursorBrush,
                        singleLine = singleLine,
                        textStyle = textStyle
                            .copy(color = if (isErrorActive.value) errorColor else contentColor),
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = Dimens.MediumPadding.size)
                            .padding(vertical = Dimens.MediumPadding.size)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isHintActive = !it.isFocused
                            }
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size),
            visible = isErrorActive.value,
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
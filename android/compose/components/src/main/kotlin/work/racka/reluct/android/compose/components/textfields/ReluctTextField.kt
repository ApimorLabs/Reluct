package work.racka.reluct.android.compose.components.textfields

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ReluctTextField(
    modifier: Modifier = Modifier,
    hint: String,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    onTextChange: (String) -> Unit = { },
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val focusRequester = remember { FocusRequester() }

    var fieldText by remember {
        mutableStateOf("")
    }

    var isHintActive by remember {
        mutableStateOf(hint.isNotEmpty())
    }
    var isTyping by remember {
        mutableStateOf(false)
    }

    val hintAlpha: Float by animateFloatAsState(
        targetValue = if (isHintActive) 1F else 0.5f
    )

    Row(
        modifier = modifier
            .clip(Shapes.large)
            .background(color = containerColor)
            .clickable {
                focusRequester.requestFocus()
            },
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
                    text = hint,
                    color = contentColor
                        .copy(alpha = hintAlpha),
                    style = textStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            BasicTextField(
                value = fieldText,
                onValueChange = {
                    fieldText = it
                    onTextChange(it)
                    isTyping = fieldText.isNotEmpty()
                },
                visualTransformation = visualTransformation,
                maxLines = maxLines,
                cursorBrush = cursorBrush,
                singleLine = singleLine,
                textStyle = textStyle
                    .copy(color = contentColor),
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
package work.racka.reluct.android.compose.components.textfields.search

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MaterialSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    hint: String = "Search here",
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    onSearch: (String) -> Unit,
    onDismissSearchClicked: () -> Unit = { },
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
    defaultPadding: Dp = Dimens.MediumPadding.size,
    smallestPadding: Dp = Dimens.ExtraSmallPadding.size,
    keyboardController: SoftwareKeyboardController? =
        LocalSoftwareKeyboardController.current,
    focusManager: FocusManager = LocalFocusManager.current,
    focusRequester: FocusRequester = remember { FocusRequester() },
    extraButton: @Composable BoxScope.() -> Unit = { },
) {
    var isHintActive by remember {
        mutableStateOf(hint.isNotEmpty())
    }
    val isTyping by remember(value) {
        mutableStateOf(value.isNotBlank())
    }

    val externalPadding: Dp by animateDpAsState(
        targetValue = if (isHintActive) defaultPadding else smallestPadding
    )

    val middlePadding: Dp by animateDpAsState(
        targetValue = if (isHintActive) {
            Dimens.SmallPadding.size
        } else {
            smallestPadding
        }
    )

    val angle: Float by animateFloatAsState(
        targetValue = if (isHintActive) -90F else 0F,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )
    val searchAndOptionsAngle: Float by animateFloatAsState(
        targetValue = if (isHintActive) 0F else 90F,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    val hintAlpha: Float by animateFloatAsState(
        targetValue = if (isHintActive) 1F else 0.5f
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = externalPadding)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = containerColor,
                    shape = shape
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Arrow/Search - Left
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (isHintActive) {
                    IconButton(
                        onClick = { focusRequester.requestFocus() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = stringResource(id = R.string.search_icon),
                            tint = contentColor,
                            modifier = Modifier
                                .rotate(searchAndOptionsAngle)
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            onDismissSearchClicked()
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_icon),
                            tint = contentColor,
                            modifier = Modifier.rotate(angle)
                        )
                    }
                }
            }

            // Search - Center
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .padding(vertical = Dimens.SmallPadding.size)
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
                            onSearch(it)
                        },
                        maxLines = 1,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        singleLine = true,
                        textStyle = textStyle.copy(color = contentColor),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                                if (value.isBlank()) {
                                    focusManager.clearFocus()
                                }
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = defaultPadding)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isHintActive = !it.isFocused
                            }
                    )
                }
            }
        }

        Spacer(Modifier.width(middlePadding))

        // Extra Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = containerColor,
                    shape = shape
                )
        ) {
            extraButton(this)
        }
    }
}

@Composable
fun PlaceholderMaterialSearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search here",
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
    extraButton: @Composable BoxScope.() -> Unit = { },
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(shape)
                .clickable { onClick() }
                .background(color = containerColor),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search - Left
            Box(
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onClick
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = stringResource(id = R.string.search_icon),
                        tint = contentColor
                    )
                }
            }

            // Search - Center
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .padding(vertical = Dimens.SmallPadding.size)
            ) {
                Text(
                    text = hint,
                    color = contentColor,
                    style = textStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(Modifier.width(Dimens.SmallPadding.size))

        // Extra Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = containerColor,
                    shape = shape
                )
        ) {
            extraButton(this)
        }
    }
}

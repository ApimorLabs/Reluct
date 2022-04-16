package work.racka.reluct.android.compose.components.textfields.search

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.ReluctAppTheme
import work.racka.reluct.android.compose.theme.Shapes

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReluctSearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search here",
    onSearch: (String) -> Unit = { },
    onOptionsClicked: () -> Unit = { },
    onDismissSearchClicked: () -> Unit = { },
    keyboardController: SoftwareKeyboardController?
    = LocalSoftwareKeyboardController.current,
    focusManager: FocusManager = LocalFocusManager.current,
    extraButton: @Composable BoxScope.() -> Unit = { }
) {
    val focusRequester = remember { FocusRequester() }
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    var isHintActive by remember {
        mutableStateOf(hint.isNotEmpty())
    }
    var isTyping by remember {
        mutableStateOf(searchText.isNotEmpty())
    }

    val externalPadding: Dp by animateDpAsState(
        targetValue = if (isHintActive) {
            Dimens.MediumPadding.size
        } else Dimens.ExtraSmallPadding.size
    )

    val middlePadding: Dp by animateDpAsState(
        targetValue = if (isHintActive) {
            Dimens.SmallPadding.size
        } else Dimens.ExtraSmallPadding.size
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
                    color = MaterialTheme.colorScheme
                        .surfaceVariant,
                    shape = Shapes.large
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
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .rotate(searchAndOptionsAngle)
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            onDismissSearchClicked()
                            searchText = ""
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            isTyping = searchText.isNotBlank()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_icon),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                            .copy(alpha = hintAlpha),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        onSearch(it)
                        isTyping = searchText.isNotBlank()
                    },
                    maxLines = 1,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleMedium
                        .copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            if (searchText.isBlank()) {
                                focusManager.clearFocus()
                            }
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = Dimens.MediumPadding.size)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            isHintActive = !it.isFocused
                        }
                )
            }
        }

        Spacer(Modifier.width(middlePadding))

        // Clear And Options - Right
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme
                        .surfaceVariant,
                    shape = Shapes.large
                )
        ) {
            if (!isHintActive) {
                IconButton(
                    onClick = {
                        onDismissSearchClicked()
                        searchText = ""
                        isTyping = searchText.isNotBlank()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(id = R.string.clear_icon),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.rotate(angle)
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        onOptionsClicked()
                        searchText = ""
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FilterList,
                        contentDescription = stringResource(id = R.string.option_icon),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.rotate(searchAndOptionsAngle)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(middlePadding))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = Shapes.large
                )
        ) {
            extraButton(this)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
private fun CustomSearchBarPreview() {
    ReluctAppTheme {
        ReluctSearchBar()
    }
}
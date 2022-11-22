package work.racka.reluct.android.screens.goals.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import work.racka.reluct.compose.common.components.R
import work.racka.reluct.compose.common.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.textfields.ReluctTextField
import work.racka.reluct.compose.common.components.textfields.texts.ListItemTitle
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@Composable
internal fun UpdateValueDialog(
    openDialog: State<Boolean>,
    onDismiss: () -> Unit,
    headingText: String,
    initialValueProvider: () -> Long,
    onSaveValue: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (openDialog.value) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            var textValue by remember { mutableStateOf(initialValueProvider().toString()) }

            Surface(
                modifier = modifier,
                shape = Shapes.large,
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                tonalElevation = 6.dp
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                    contentPadding = PaddingValues(Dimens.MediumPadding.size)
                ) {
                    item {
                        ListItemTitle(text = headingText)
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
                        ) {
                            ReluctTextField(
                                value = textValue,
                                isError = textValue.isBlank(),
                                errorText = "",
                                singleLine = true,
                                hint = stringResource(R.string.enter_current_value),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                onTextChange = { text -> textValue = text },
                                textStyle = MaterialTheme.typography.titleMedium
                                    .copy(textAlign = TextAlign.Center),
                                modifier = Modifier.fillMaxWidth(.5f)
                            )

                            ReluctButton(
                                modifier = Modifier.weight(1f),
                                buttonText = stringResource(id = R.string.save_button_text),
                                icon = Icons.Rounded.Save,
                                shape = Shapes.large,
                                buttonColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                onButtonClicked = {
                                    if (textValue.isNotBlank()) {
                                        textValue.toLongOrNull()?.run {
                                            onSaveValue(this)
                                            onDismiss()
                                        } ?: run { textValue = "" }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

package work.racka.reluct.android.screens.screentime.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.model.domain.limits.AppTimeLimit
import work.racka.reluct.compose.common.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.numberPicker.FullHours
import work.racka.reluct.compose.common.components.numberPicker.Hours
import work.racka.reluct.compose.common.components.numberPicker.HoursNumberPicker
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@Composable
fun AppTimeLimitDialog(
    onDismiss: () -> Unit,
    initialAppTimeLimit: AppTimeLimit,
    onSaveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = modifier.size(300.dp),
            shape = Shapes.large,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = 6.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                var pickerValue by remember {
                    mutableStateOf<Hours>(
                        FullHours(
                            hours = initialAppTimeLimit.hours,
                            minutes = initialAppTimeLimit.minutes
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(Dimens.MediumPadding.size)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppNameEntry(
                        appName = initialAppTimeLimit.appInfo.appName,
                        icon = initialAppTimeLimit.appInfo.appIcon.icon
                    )

                    HoursNumberPicker(
                        value = pickerValue,
                        onValueChange = { pickerValue = it },
                        dividersColor = contentColor,
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            color = contentColor
                        ),
                        hoursDivider = {
                            Text(
                                text = "hr",
                                style = MaterialTheme.typography.bodyLarge
                                    .copy(color = contentColor)
                            )
                        },
                        minutesDivider = {
                            Text(
                                text = "m",
                                style = MaterialTheme.typography.bodyLarge
                                    .copy(color = contentColor)
                            )
                        }
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        ReluctButton(
                            buttonText = stringResource(id = R.string.save_button_text),
                            icon = Icons.Rounded.Done,
                            onButtonClicked = {
                                onSaveTimeLimit(pickerValue.hours, pickerValue.minutes)
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}

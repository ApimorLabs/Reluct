package work.racka.reluct.android.screens.screentime.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.number_picker.FullHours
import work.racka.reluct.android.compose.components.number_picker.Hours
import work.racka.reluct.android.compose.components.number_picker.HoursNumberPicker
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.screen_time.limits.states.AppTimeLimitState

@Composable
fun AppTimeLimitDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    appTimeLimitState: AppTimeLimitState,
    onSaveTimeLimit: (hours: Int, minutes: Int) -> Unit,
) {
    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = modifier.animateContentSize(),
            shape = Shapes.large,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = 6.dp
        ) {
            when (appTimeLimitState) {
                is AppTimeLimitState.Data -> {
                    val initialAppTimeLimit = appTimeLimitState.timeLimit
                    var pickerValue by remember {
                        mutableStateOf<Hours>(
                            FullHours(
                                hours = initialAppTimeLimit.hours,
                                minutes = initialAppTimeLimit.minutes
                            )
                        )
                    }

                    Column(
                        modifier = Modifier.padding(Dimens.MediumPadding.size),
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
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = contentColor
                            )
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
                else -> {
                    Box(contentAlignment = Alignment.Center) {
                        LinearProgressIndicator(
                            Modifier.padding(Dimens.ExtraLargePadding.size)
                        )
                    }
                }
            }
        }
    }
}
package work.racka.reluct.android.screens.onboarding.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import work.racka.reluct.android.compose.components.buttons.OutlinedReluctButton
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.onboarding.states.OnBoardingPages
import work.racka.reluct.common.features.onboarding.states.OnBoardingState

@Composable
internal fun OnBoardingBottomBar(
    modifier: Modifier = Modifier,
    uiState: OnBoardingState,
    onUpdatePage: (OnBoardingPages) -> Unit,
    onCompleted: () -> Unit
) {
    val context = LocalContext.current
    val bottomButtons by remember(uiState.currentPage, uiState.permissionsState) {
        derivedStateOf {
            when (uiState.currentPage) {
                is OnBoardingPages.Welcome -> {
                    BottomButtonsProperties(
                        positiveText = context.getString(R.string.next_text),
                        isPositiveEnabled = true,
                        positiveAction = { onUpdatePage(OnBoardingPages.Permissions) },
                        negativeText = "",
                        isNegativeEnabled = false,
                        negativeAction = {}
                    )
                }
                is OnBoardingPages.Permissions -> {
                    BottomButtonsProperties(
                        positiveText = context.getString(R.string.next_text),
                        isPositiveEnabled = true,
                        positiveAction = { onUpdatePage(OnBoardingPages.Notifications) },
                        negativeText = context.getString(R.string.back_text),
                        isNegativeEnabled = true,
                        negativeAction = { onUpdatePage(OnBoardingPages.Welcome) }
                    )
                }
                is OnBoardingPages.Notifications -> {
                    BottomButtonsProperties(
                        positiveText = context.getString(R.string.next_text),
                        isPositiveEnabled = uiState.permissionsState.notificationGranted,
                        positiveAction = { onUpdatePage(OnBoardingPages.Reminders) },
                        negativeText = context.getString(R.string.back_text),
                        isNegativeEnabled = true,
                        negativeAction = { onUpdatePage(OnBoardingPages.Permissions) }
                    )
                }
                is OnBoardingPages.Reminders -> {
                    BottomButtonsProperties(
                        positiveText = context.getString(R.string.next_text),
                        isPositiveEnabled = uiState.permissionsState.alarmsAndRemindersGranted,
                        positiveAction = { onUpdatePage(OnBoardingPages.UsageAccess) },
                        negativeText = context.getString(R.string.back_text),
                        isNegativeEnabled = true,
                        negativeAction = { onUpdatePage(OnBoardingPages.Notifications) }
                    )
                }
                is OnBoardingPages.UsageAccess -> {
                    BottomButtonsProperties(
                        positiveText = context.getString(R.string.next_text),
                        isPositiveEnabled = uiState.permissionsState.usageAccessGranted,
                        positiveAction = { onUpdatePage(OnBoardingPages.Overlay) },
                        negativeText = context.getString(R.string.back_text),
                        isNegativeEnabled = true,
                        negativeAction = { onUpdatePage(OnBoardingPages.Reminders) }
                    )
                }
                is OnBoardingPages.Overlay -> {
                    BottomButtonsProperties(
                        positiveText = context.getString(R.string.next_text),
                        isPositiveEnabled = uiState.permissionsState.overlayGranted,
                        positiveAction = { onUpdatePage(OnBoardingPages.Themes) },
                        negativeText = context.getString(R.string.back_text),
                        isNegativeEnabled = true,
                        negativeAction = { onUpdatePage(OnBoardingPages.UsageAccess) }
                    )
                }
                is OnBoardingPages.Themes -> {
                    BottomButtonsProperties(
                        positiveText = context.getString(R.string.next_text),
                        isPositiveEnabled = true,
                        positiveAction = { onUpdatePage(OnBoardingPages.AllSet) },
                        negativeText = context.getString(R.string.back_text),
                        isNegativeEnabled = true,
                        negativeAction = { onUpdatePage(OnBoardingPages.Overlay) }
                    )
                }
                is OnBoardingPages.AllSet -> {
                    BottomButtonsProperties(
                        positiveText = context.getString(R.string.continue_text),
                        isPositiveEnabled = true,
                        positiveAction = { onCompleted() },
                        negativeText = "",
                        isNegativeEnabled = false,
                        negativeAction = { }
                    )
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .padding(vertical = Dimens.SmallPadding.size)
            .fillMaxWidth() then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Put Text
        if (uiState.currentPage is OnBoardingPages.Welcome) {
            Text(text = "Privacy Policy and Terms of Service", textAlign = TextAlign.Center)
        }

        Row(
            modifier = (if (bottomButtons.isNegativeEnabled) Modifier.fillMaxWidth() else Modifier)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (bottomButtons.isNegativeEnabled) {
                OutlinedReluctButton(
                    buttonText = bottomButtons.negativeText,
                    icon = null,
                    onButtonClicked = bottomButtons.negativeAction
                )
            }

            ReluctButton(
                buttonText = bottomButtons.positiveText,
                icon = null,
                enabled = bottomButtons.isPositiveEnabled,
                onButtonClicked = bottomButtons.positiveAction
            )
        }
    }
}

@Stable
private data class BottomButtonsProperties(
    val positiveText: String,
    val isPositiveEnabled: Boolean,
    val positiveAction: () -> Unit,
    val negativeText: String,
    val isNegativeEnabled: Boolean,
    val negativeAction: () -> Unit
)
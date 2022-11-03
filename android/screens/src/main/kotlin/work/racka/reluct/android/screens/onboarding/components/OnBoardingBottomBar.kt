package work.racka.reluct.android.screens.onboarding.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.collections.immutable.persistentListOf
import work.racka.reluct.android.compose.components.buttons.OutlinedReluctButton
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.textfields.texts.HighlightTextProps
import work.racka.reluct.android.compose.components.textfields.texts.HyperlinkText
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.util.isAndroid13Plus
import work.racka.reluct.common.features.onboarding.states.OnBoardingPages
import work.racka.reluct.common.features.onboarding.states.OnBoardingState

@Composable
internal fun OnBoardingBottomBar(
    uiState: OnBoardingState,
    onUpdatePage: (OnBoardingPages) -> Unit,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val bottomButtons by remember(
        uiState.currentPage,
        uiState.permissionsState,
        uiState.appBlockingEnabled
    ) {
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
                        positiveAction = {
                            if (isAndroid13Plus()) {
                                onUpdatePage(OnBoardingPages.Notifications)
                            } else {
                                onUpdatePage(OnBoardingPages.UsageAccess)
                            }
                        },
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
                        negativeAction = {
                            if (isAndroid13Plus()) {
                                onUpdatePage(OnBoardingPages.Reminders)
                            } else {
                                onUpdatePage(OnBoardingPages.Permissions)
                            }
                        }
                    )
                }
                is OnBoardingPages.Overlay -> {
                    BottomButtonsProperties(
                        positiveText = context.getString(R.string.next_text),
                        isPositiveEnabled = uiState.permissionsState.overlayGranted ||
                            !uiState.appBlockingEnabled,
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
        // Privacy Policy and Terms of Service
        if (uiState.currentPage is OnBoardingPages.Welcome) {
            HyperlinkText(
                fullText = stringResource(id = R.string.privacy_policy_terms_text),
                textAlign = TextAlign.Center,
                hyperLinks = persistentListOf(
                    HighlightTextProps(
                        text = stringResource(id = R.string.privacy_policy_hyperlink_text),
                        url = stringResource(id = R.string.privacy_policy_hyperlink_url),
                        color = MaterialTheme.colorScheme.primary
                    ),
                    HighlightTextProps(
                        text = stringResource(id = R.string.terms_of_service_hyperlink_text),
                        url = stringResource(id = R.string.terms_of_service_hyperlink_url),
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            )
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

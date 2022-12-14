package work.racka.reluct.android.screens.authentication.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ReplayCircleFilled
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.persistentListOf
import work.racka.reluct.android.screens.R
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.buttons.OutlinedReluctButton
import work.racka.reluct.compose.common.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.compose.common.components.resources.painterResource
import work.racka.reluct.compose.common.components.textfields.texts.HighlightTextProps
import work.racka.reluct.compose.common.components.textfields.texts.HyperlinkText
import work.racka.reluct.compose.common.theme.Dimens

@Composable
internal fun VerifyEmailPage(
    userEmail: String,
    isEmailVerified: Boolean,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onResendEmail: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawableSize = 200.dp
    val buttonRatio = .9f // 90%

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.LargePadding.size) then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.padding(top = Dimens.ExtraLargePadding.size))
        }

        // Illustrations
        item {
            Image(
                modifier = Modifier
                    .size(drawableSize),
                painter = if (isEmailVerified) {
                    painterResource(SharedRes.assets.email_done)
                } else {
                    painterResource(SharedRes.assets.email_opened)
                },
                contentDescription = null
            )
        }

        // Title
        item {
            ListGroupHeadingHeader(
                text = if (isEmailVerified) {
                    stringResource(id = R.string.verify_email_done_text)
                } else {
                    stringResource(id = R.string.verify_email_text)
                },
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 35.sp)
            )
            Text(
                text = userEmail,
                textAlign = TextAlign.Center,
                color = LocalContentColor.current.copy(.7f)
            )
        }

        // Description
        item {
            Text(
                text = if (isEmailVerified) {
                    stringResource(id = R.string.verify_email_done_description)
                } else {
                    stringResource(id = R.string.verify_email_description)
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = LocalContentColor.current.copy(.7f),
                modifier = Modifier.fillMaxWidth(buttonRatio)
            )
        }

        // Helper link
        if (!isEmailVerified) {
            item {
                HyperlinkText(
                    fullText = stringResource(id = R.string.find_gmail_spam),
                    textAlign = TextAlign.Center,
                    textStyle = MaterialTheme.typography.titleLarge,
                    hyperLinks = persistentListOf(
                        HighlightTextProps(
                            text = stringResource(id = R.string.find_gmail_spam),
                            url = stringResource(id = R.string.find_gmail_spam_link),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    )
                )
            }
        }

        // Buttons
        item {
            if (isEmailVerified) {
                // Continue
                ReluctButton(
                    buttonText = stringResource(R.string.continue_text),
                    icon = Icons.Rounded.CheckCircle,
                    onButtonClicked = onContinue,
                    modifier = Modifier.fillMaxSize(buttonRatio)
                )
            } else {
                // Refresh
                ReluctButton(
                    buttonText = stringResource(R.string.refresh_verification_text),
                    icon = Icons.Rounded.ReplayCircleFilled,
                    onButtonClicked = onRefresh,
                    showLoading = isLoading,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxSize(buttonRatio)
                )
                Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                // Resend Email
                OutlinedReluctButton(
                    buttonText = stringResource(R.string.resend_email_text),
                    icon = Icons.Rounded.Send,
                    onButtonClicked = onResendEmail,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxSize(buttonRatio)
                )
            }
        }

        item { Spacer(modifier = Modifier.navigationBarsPadding()) }
    }
}

package work.racka.reluct.android.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.collections.immutable.persistentListOf
import work.racka.reluct.android.screens.BuildConfig
import work.racka.reluct.android.screens.R
import work.racka.reluct.compose.common.components.textfields.texts.HighlightTextProps
import work.racka.reluct.compose.common.components.textfields.texts.HyperlinkText
import work.racka.reluct.compose.common.theme.Dimens

@Composable
fun AppAboutInfo(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.developer_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(id = R.string.copyright_year),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(id = R.string.app_version, BuildConfig.appVersionName),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = stringResource(id = R.string.made_with_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )

        // Terms of Service and Privacy Policy
        HyperlinkText(
            fullText = stringResource(id = R.string.privacy_policy_n_terms_hyperlink_text),
            textAlign = TextAlign.Center,
            hyperLinks = persistentListOf(
                HighlightTextProps(
                    text = stringResource(id = R.string.privacy_policy_n_terms_hyperlink_text),
                    url = stringResource(id = R.string.reluct_polices_hyperlink_url),
                    color = MaterialTheme.colorScheme.primary
                )
            )
        )
    }
}

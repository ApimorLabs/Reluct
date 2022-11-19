package work.racka.reluct.android.screens.onboarding.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.persistentListOf
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.components.textfields.texts.HighlightTextProps
import work.racka.reluct.android.compose.components.textfields.texts.HighlightedText
import work.racka.reluct.android.screens.R
import work.racka.reluct.compose.common.theme.Dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun WelcomePage(
    modifier: Modifier = Modifier
) {
    val drawableSize = 400.dp

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.LargePadding.size) then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            ListGroupHeadingHeader(
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 40.sp)
            )
        }

        item {
            Image(
                modifier = Modifier
                    .size(drawableSize)
                    .padding(Dimens.MediumPadding.size),
                painter = painterResource(id = R.drawable.welcome_mobile),
                contentDescription = null
            )
        }

        item {
            HighlightedText(
                fullText = stringResource(id = R.string.welcome_text),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.titleLarge,
                highlights = persistentListOf(
                    HighlightTextProps(
                        text = stringResource(id = R.string.tasks_highlight_text),
                        color = MaterialTheme.colorScheme.primary
                    ),
                    HighlightTextProps(
                        text = stringResource(id = R.string.goals_highlight_text),
                        color = Color.Green
                    )
                )
            )
        }
    }
}

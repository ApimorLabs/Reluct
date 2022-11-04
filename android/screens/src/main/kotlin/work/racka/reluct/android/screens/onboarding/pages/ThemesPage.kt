package work.racka.reluct.android.screens.onboarding.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.settings.components.ThemeSelectCard
import work.racka.reluct.android.screens.settings.components.getThemes
import work.racka.reluct.android.screens.util.BackPressHandler

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ThemesPage(
    selectedTheme: Int,
    onSelectTheme: (themeValue: Int) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackPressHandler { goBack() } // Handle Back Presses

    val drawableSize = 250.dp

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.LargePadding.size) then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = Dimens.MediumPadding.size)
    ) {
        stickyHeader {
            ListGroupHeadingHeader(
                text = stringResource(id = R.string.themes_text),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 40.sp)
            )
        }

        item {
            Image(
                modifier = Modifier
                    .size(drawableSize),
                painter = painterResource(id = R.drawable.theme_change),
                contentDescription = null
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.themes_desc_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(items = getThemes()) { item ->
            ThemeSelectCard(
                themeData = item,
                isSelected = item.theme.themeValue == selectedTheme,
                onSelectTheme = onSelectTheme
            )
        }
    }
}

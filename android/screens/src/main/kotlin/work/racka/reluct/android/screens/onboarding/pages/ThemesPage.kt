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
import work.racka.reluct.android.compose.theme.Theme
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.onboarding.components.ThemeHolder
import work.racka.reluct.android.screens.onboarding.components.ThemeSelectCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ThemesPage(
    modifier: Modifier = Modifier,
    selectedTheme: Int,
    onSelectTheme: (themeValue: Int) -> Unit
) {

    val drawableSize = 300.dp

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
                    .size(drawableSize)
                    .padding(Dimens.MediumPadding.size),
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

        items(items = themes) { item ->
            ThemeSelectCard(
                themeData = item,
                isSelected = item.theme.themeValue == selectedTheme,
                onSelectTheme = onSelectTheme
            )
        }
    }
}

private val themes = arrayOf(
    ThemeHolder(
        theme = Theme.FOLLOW_SYSTEM,
        themeNameResId = R.string.default_theme_system,
        themeDescriptionResId = R.string.default_theme_system_desc
    ),
    ThemeHolder(
        theme = Theme.MATERIAL_YOU,
        themeNameResId = R.string.material_you_theme_text,
        themeDescriptionResId = R.string.material_you_theme_text_desc
    ),
    ThemeHolder(
        theme = Theme.LIGHT_THEME,
        themeNameResId = R.string.light_theme_text,
        themeDescriptionResId = R.string.light_theme_text_desc
    ),
    ThemeHolder(
        theme = Theme.DARK_THEME,
        themeNameResId = R.string.dark_theme_text,
        themeDescriptionResId = R.string.dark_theme_text_desc
    )
)
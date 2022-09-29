package work.racka.reluct.android.screens.onboarding.components

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import work.racka.reluct.android.compose.components.cards.card_with_actions.ReluctDescriptionCard
import work.racka.reluct.android.compose.theme.Theme
import work.racka.reluct.android.screens.R

@Composable
internal fun ThemeSelectCard(
    modifier: Modifier = Modifier,
    themeData: ThemeHolder,
    isSelected: Boolean,
    onSelectTheme: (themeValue: Int) -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant
    )

    ReluctDescriptionCard(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        title = {
            Text(
                text = stringResource(id = themeData.themeNameResId),
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current
            )
        },
        description = {
            Text(
                text = stringResource(id = themeData.themeDescriptionResId),
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current.copy(alpha = .8f)
            )
        },
        icon = themeData.theme.icon,
        onClick = { onSelectTheme(themeData.theme.themeValue) }
    )
}

internal data class ThemeHolder(
    val theme: Theme,
    @StringRes val themeNameResId: Int,
    @StringRes val themeDescriptionResId: Int,
)

internal fun getThemes() = arrayOf(
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
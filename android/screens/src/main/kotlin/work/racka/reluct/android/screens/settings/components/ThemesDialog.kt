package work.racka.reluct.android.screens.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R

@Composable
fun ThemesDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    currentTheme: Int,
    onSaveTheme: (value: Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val bottomButtonHeight = Dimens.LargePadding.size

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = modifier,
            shape = Shapes.large,
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 6.dp
        ) {
            Box(
                modifier = Modifier
                    .padding(Dimens.MediumPadding.size),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .padding(bottom = bottomButtonHeight)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ListGroupHeadingHeader(text = stringResource(id = R.string.themes_text))

                    getThemes().forEach { item ->
                        ThemeSelectCard(
                            themeData = item,
                            isSelected = currentTheme == item.theme.themeValue,
                            onSelectTheme = onSaveTheme
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .height(bottomButtonHeight)
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    ReluctButton(
                        buttonText = stringResource(id = R.string.ok),
                        icon = Icons.Rounded.Done,
                        onButtonClicked = onDismiss
                    )
                }
            }
        }
    }
}
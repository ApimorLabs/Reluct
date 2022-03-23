package work.racka.reluct.android.compose.components.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.ReluctAppTheme
import work.racka.reluct.android.compose.theme.Typography

@Composable
internal fun TabEntry(
    modifier: Modifier = Modifier,
    title: String,
    textColor: Color,
    onClick: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .zIndex(2f)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(
                color = Color.Transparent
            )
            .padding(Dimens.MediumPadding.size),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = Typography.titleMedium,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun TabEntryPreview() {
    ReluctAppTheme {
        TabEntry(
            title = "Overview",
            textColor = MaterialTheme.colorScheme.onBackground
        )
    }
}
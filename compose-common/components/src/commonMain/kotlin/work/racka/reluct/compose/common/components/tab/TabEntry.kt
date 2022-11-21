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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.ReluctAppTheme

@Composable
fun TabEntry(
    title: String,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .zIndex(2f)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(
                color = Color.Transparent
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = Dimens.SmallPadding.size,
                vertical = Dimens.SmallPadding.size + Dimens.ExtraSmallPadding.size
            ),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun TabEntryPreview() {
    ReluctAppTheme {
        TabEntry(
            title = "Overview",
            textColor = MaterialTheme.colorScheme.onBackground
        )
    }
}

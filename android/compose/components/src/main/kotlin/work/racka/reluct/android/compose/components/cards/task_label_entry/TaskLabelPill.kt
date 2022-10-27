package work.racka.reluct.android.compose.components.cards.task_label_entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import work.racka.reluct.android.compose.components.util.getContentColor
import work.racka.reluct.android.compose.components.util.toColor
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes

@Composable
fun TaskLabelPill(
    modifier: Modifier = Modifier,
    name: String,
    colorHex: String
) {
    val labelColors by remember(colorHex) {
        derivedStateOf {
            val color = colorHex.toColor()
            val contentColor = color.getContentColor()
            color to contentColor
        }
    }

    Box(
        modifier = Modifier
            .clip(Shapes.large)
            .background(labelColors.first) then modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = Dimens.SmallPadding.size, horizontal = Dimens.MediumPadding.size
            ),
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = labelColors.second,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
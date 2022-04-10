package work.racka.reluct.android.compose.components.cards.task_entry

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.ReluctAppTheme

@Composable
internal fun TaskHeading(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
internal fun TaskDescription(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TaskTimeInfo(
    modifier: Modifier = Modifier,
    timeText: String,
    showOverdueLabel: Boolean = false,
    overdue: Boolean = false,
) {

    Row(modifier = modifier) {
        Text(
            text = timeText,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = LocalContentColor.current.copy(alpha = .9f)
        )

        if (showOverdueLabel) {
            Spacer(modifier = Modifier.width(Dimens.ExtraSmallPadding.size))
            Text(
                text = "-",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(Dimens.ExtraSmallPadding.size))
            Text(
                text = if (overdue) stringResource(id = R.string.overdue_text)
                else stringResource(id = R.string.in_time_text),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (overdue) MaterialTheme.colorScheme.error else Color.Green
            )
        }
    }
}

@Preview
@Composable
internal fun LabelsPreview() {
    ReluctAppTheme {
        Surface {
            Column {
                TaskHeading(text = "Tasks Title Here")
                Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding.size))
                TaskDescription(text = "This is a task description and it it really long. This is a task description and it it really long. This is a task description and it it really long. This is a task description and it it really long. This is a task description and it it really long.")
                Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding.size))
                TaskTimeInfo(timeText = "In 3 hrs")
                TaskTimeInfo(timeText = "Thu, 20 Feb", showOverdueLabel = true)
                TaskTimeInfo(timeText = "Thu, 20 Feb", showOverdueLabel = true, overdue = true)
            }
        }
    }
}
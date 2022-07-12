package work.racka.reluct.widgets.components.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.widgets.core.GlanceTheme

@Composable
internal fun TaskHeading(
    modifier: GlanceModifier = GlanceModifier,
    text: String,
    isOverdue: Boolean
) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            color = if (isOverdue) GlanceTheme.colors.onError else GlanceTheme.colors.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        maxLines = 1
    )
}

@Composable
internal fun TaskDescription(
    modifier: GlanceModifier = GlanceModifier,
    text: String,
    isOverdue: Boolean
) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            color = if (isOverdue) GlanceTheme.colors.onError else GlanceTheme.colors.onSurface,
            fontSize = 14.sp,
        ),
        maxLines = 2
    )
}

@Composable
internal fun TaskTimeInfo(
    modifier: GlanceModifier = GlanceModifier,
    timeText: String,
    showOverdueLabel: Boolean = false,
    overdue: Boolean = false,
) {

    Row(modifier = modifier) {
        Text(
            text = timeText,
            style = TextStyle(
                color = if (overdue) GlanceTheme.colors.onError else GlanceTheme.colors.onSurface,
                fontSize = 12.sp,
            ),
            maxLines = 1
        )

        if (showOverdueLabel) {
            Spacer(modifier = GlanceModifier.width(Dimens.ExtraSmallPadding.size))
            Text(
                text = "-",
                style = TextStyle(
                    color = if (overdue) GlanceTheme.colors.onError else GlanceTheme.colors.onSurface,
                    fontSize = 12.sp,
                )
            )
            Spacer(modifier = GlanceModifier.width(Dimens.ExtraSmallPadding.size))
            Text(
                text = if (overdue) "Overdue"
                else "In Time",
                style = TextStyle(
                    color = if (overdue) GlanceTheme.colors.onError else GlanceTheme.colors.onSurface,
                    fontSize = 12.sp,
                ),
                maxLines = 1
            )
        }
    }
}
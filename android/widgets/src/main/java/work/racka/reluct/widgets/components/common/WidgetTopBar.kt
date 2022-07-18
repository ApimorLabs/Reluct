package work.racka.reluct.widgets.components.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.widgets.core.GlanceTheme

@Composable
fun WidgetTopBar(
    modifier: GlanceModifier = GlanceModifier,
    title: String,
    actionButton: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(GlanceTheme.colors.primary),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = GlanceModifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.Start
        ) {
            Text(
                modifier = GlanceModifier
                    .defaultWeight(),
                text = title,
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(GlanceModifier.width(Dimens.MediumPadding.size))
            actionButton()
        }
    }
}
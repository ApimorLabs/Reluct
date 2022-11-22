package work.racka.reluct.widgets.components.common

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box

@Composable
fun WidgetIconButton(
    onClick: Action,
    icon: @Composable () -> Unit,
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(
        modifier = GlanceModifier.clickable(onClick) then modifier,
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

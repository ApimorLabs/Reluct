package work.racka.reluct.widgets.components.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box

@Composable
internal fun RoundCheckbox(
    modifier: GlanceModifier = GlanceModifier,
    isChecked: Boolean,
    clickCheckBoxAction: Action,
) {

    Box(
        modifier = modifier.cornerRadius(15.dp)
            .clickable(onClick = clickCheckBoxAction),
        contentAlignment = Alignment.Center
    ) {
        CheckBox(checked = isChecked, onCheckedChange = clickCheckBoxAction)
        /*if(isChecked) {
            Image(
                modifier = modifier,
                provider = IconImageProvider(),
                contentDescription = "Checkbox check"
            )
        } else {
            Icon(
                modifier = modifier,
                imageVector = Icons.Rounded.RadioButtonUnchecked,
                contentDescription = stringResource(id = R.string.checkbox_unchecked)
            )
        }*/
    }
}
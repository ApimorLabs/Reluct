package work.racka.reluct.android.screens.screentime.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import work.racka.reluct.android.compose.components.buttons.ValueOffsetButton
import work.racka.reluct.common.features.screenTime.statistics.states.ScreenTimeStatsSelectedInfo
import work.racka.reluct.compose.common.theme.Shapes

@Composable
internal fun ScreenTimeWeekSelectorButton(
    selectedInfoProvider: () -> ScreenTimeStatsSelectedInfo,
    onUpdateWeekOffset: (offset: Int) -> Unit
) {
    val selectedInfo = remember { derivedStateOf { selectedInfoProvider() } }
    ValueOffsetButton(
        text = selectedInfo.value.selectedWeekText,
        offsetValue = selectedInfo.value.weekOffset,
        onOffsetValueChange = onUpdateWeekOffset,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = Shapes.large,
        incrementEnabled = selectedInfo.value.weekOffset < 0
    )
}

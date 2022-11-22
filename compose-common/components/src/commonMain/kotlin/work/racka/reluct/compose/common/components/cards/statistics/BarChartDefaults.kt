package work.racka.reluct.compose.common.components.cards.statistics

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

object BarChartDefaults {

    val barColor
        @Composable get() = MaterialTheme.colorScheme.secondary.copy(alpha = .7f)
}

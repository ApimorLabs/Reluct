package work.racka.reluct.common.settings

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class AutoFocusMode(
    val enabled: Boolean,
    val timeRange: ClosedRange<LocalTime>
)

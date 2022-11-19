package work.racka.reluct.compose.common.date.time.picker.core

import androidx.compose.runtime.Stable

@Stable
data class DateTimeDialogButtonText(
    val positiveText: String? = "OK",
    val negativeText: String? = "CANCEL"
)
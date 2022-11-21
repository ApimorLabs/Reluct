package work.racka.reluct.android.compose.components.util

import androidx.compose.ui.graphics.Color
import work.racka.reluct.common.model.domain.core.Icon

expect fun Icon.extractColor(defaultColor: Color = Color.Gray): Color

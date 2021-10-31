package work.racka.reluct.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import work.racka.reluct.ui.theme.Dimens

@Composable
fun PermCheck(
    isGranted: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Permission Granted: ")
        Spacer(modifier = Modifier.width(Dimens.SmallPadding.size))
        Box(
            modifier = Modifier
                .size(Dimens.MediumPadding.size)
                .clip(CircleShape)
                .background(
                    color = if (isGranted) Color.Green else Color.Red
                )
        )
    }
}
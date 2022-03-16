package work.racka.reluct.ui.components.summary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.racka.reluct.Dimens
import work.racka.reluct.ReluctAppTheme
import work.racka.reluct.model.UsageStats

@Composable
fun SummaryPills(
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    stats: List<UsageStats>,
    availableEvents: String,
    onUnlockClick: () -> Unit = { },
    onEventsClick: () -> Unit = { }
) {
    val unlockCount = remember(stats) {
        mutableStateOf(0L)
    }
    stats.forEach { stat ->
        unlockCount.value += stat.unlockCount
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Button(
            onClick = onUnlockClick,
            shape = CircleShape,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                contentColor = contentColor,
                containerColor = backgroundColor
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.LockOpen,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = unlockCount.value.toString(),
                modifier = Modifier
                    .padding(
                        horizontal = 4.dp,
                        vertical = Dimens.SmallPadding.size
                    ),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))

        Button(
            onClick = onEventsClick,
            shape = CircleShape,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                contentColor = contentColor,
                containerColor = backgroundColor
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Event,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = availableEvents,
                modifier = Modifier
                    .padding(
                        horizontal = 4.dp,
                        vertical = Dimens.SmallPadding.size
                    ),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun SummaryPillsPreview() {
    ReluctAppTheme {
        SummaryPills(stats = listOf(), availableEvents = "5")
    }
}

package work.racka.reluct.android.screens.goals.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.screentime.components.AppNameEntry
import work.racka.reluct.common.model.domain.appInfo.AppInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppsListCard(
    modifier: Modifier = Modifier,
    apps: List<AppInfo>,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = shape
    ) {
        Column(
            modifier = Modifier.padding(Dimens.MediumPadding.size),
            verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            apps.forEach { app ->
                AppNameEntry(
                    appName = app.appName,
                    icon = app.appIcon.icon,
                    contentColor = contentColor,
                    iconSize = 36.dp
                )
            }
        }
    }
}
package work.racka.reluct.android.compose.components.topBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ReluctSmallTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults
        .smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = LocalContentColor.current,
            titleContentColor = LocalContentColor.current,
            actionIconContentColor = LocalContentColor.current
        ),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    Box(
        modifier = Modifier
            .background(
                colors.containerColor(
                    scrollFraction = scrollBehavior?.scrollFraction ?: 0f
                ).value
            ),
        contentAlignment = Alignment.Center
    ) {
        SmallTopAppBar(
            modifier = modifier,
            title = {
                Text(text = title)
            },
            navigationIcon = navigationIcon,
            actions = actions,
            colors = colors,
            scrollBehavior = scrollBehavior
        )
    }
}
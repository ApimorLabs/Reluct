package work.racka.reluct.android.compose.components.topBar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReluctSmallTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    titleTextStyle: TextStyle = MaterialTheme.typography.headlineSmall,
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
    TopAppBar(
        modifier = modifier,
        title = { Text(text = title, style = titleTextStyle) },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
        scrollBehavior = scrollBehavior
    )
}
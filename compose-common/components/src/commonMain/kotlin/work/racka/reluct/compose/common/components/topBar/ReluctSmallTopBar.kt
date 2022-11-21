package work.racka.reluct.android.compose.components.topBar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.TopAppBar // TODO: Change to M3
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun ReluctSmallTopAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = MaterialTheme.typography.headlineSmall,
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
        // TODO: Remove when M3 is update in MPP compose
        backgroundColor = colors.containerColor(0f).value,
        contentColor = colors.titleContentColor(0f).value
        /*
        TODO: Reactivate when M3 is updated
        colors = colors,
        scrollBehavior = scrollBehavior
        */
    )
}

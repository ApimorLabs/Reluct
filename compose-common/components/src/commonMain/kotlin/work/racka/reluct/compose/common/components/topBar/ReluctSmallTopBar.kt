package work.racka.reluct.compose.common.components.topBar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.TopAppBar // TODO: Change to M3
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun ReluctSmallTopAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    actions: @Composable RowScope.() -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor)
    /*colors: TopAppBarColors = TopAppBarDefaults
        .smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = LocalContentColor.current,
            titleContentColor = LocalContentColor.current,
            actionIconContentColor = LocalContentColor.current
        ),
    scrollBehavior: TopAppBarScrollBehavior? = null,*/
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = title, style = titleTextStyle) },
        navigationIcon = navigationIcon,
        actions = actions,
        // TODO: Remove when M3 is update in MPP compose
        backgroundColor = containerColor,
        contentColor = contentColor
        /*
        TODO: Reactivate when M3 is updated
        colors = colors,
        scrollBehavior = scrollBehavior
        */
    )
}

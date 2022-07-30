package work.racka.reluct.android.screens.screentime.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowDown
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.model.domain.app_info.AppInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAppsDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    topItemsHeading: String,
    bottomItemsHeading: String,
    topItems: List<AppInfo>,
    bottomItems: List<AppInfo>,
    onTopItemClicked: (app: AppInfo) -> Unit,
    onBottomItemClicked: (app: AppInfo) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Scaffold(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.MediumPadding.size),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    ReluctButton(
                        buttonText = stringResource(id = R.string.ok),
                        icon = Icons.Rounded.Done,
                        onButtonClicked = onDismiss
                    )
                }
            }
        ) { paddingValues ->
            ManageAppsUI(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(Dimens.MediumPadding.size),
                topItemsHeading = topItemsHeading,
                bottomItemsHeading = bottomItemsHeading,
                topItems = topItems,
                bottomItems = bottomItems,
                onTopItemClicked = onTopItemClicked,
                onBottomItemClicked = onBottomItemClicked
            )
        }
    }
}

/** This is a LazyColum, be careful when nesting it inside other Columns **/
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ManageAppsUI(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    topItemsHeading: String,
    bottomItemsHeading: String,
    topItems: List<AppInfo>,
    bottomItems: List<AppInfo>,
    onTopItemClicked: (app: AppInfo) -> Unit,
    onBottomItemClicked: (app: AppInfo) -> Unit,
    contentColor: Color = LocalContentColor.current
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        stickyHeader {
            ListGroupHeadingHeader(text = topItemsHeading)
        }

        if (topItems.isEmpty()) {
            item {
                Text(
                    modifier = Modifier
                        .padding(Dimens.LargePadding.size),
                    text = stringResource(R.string.no_apps_text),
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor,
                    textAlign = TextAlign.Center
                )
            }
        }

        items(items = topItems, key = { it.packageName }) { item ->
            AppNameEntry(
                modifier = Modifier
                    .animateItemPlacement()
                    .clip(Shapes.large)
                    .clickable { onTopItemClicked(item) },
                appName = item.appName,
                icon = item.appIcon.icon,
                contentColor = contentColor
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardDoubleArrowDown,
                    contentDescription = "Move Down",
                    tint = contentColor
                )
            }
        }

        stickyHeader {
            ListGroupHeadingHeader(text = bottomItemsHeading)
        }

        if (topItems.isEmpty()) {
            item {
                Text(
                    modifier = Modifier
                        .padding(Dimens.LargePadding.size),
                    text = stringResource(R.string.no_apps_text),
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor,
                    textAlign = TextAlign.Center
                )
            }
        }

        items(items = bottomItems, key = { it.packageName }) { item ->
            AppNameEntry(
                modifier = Modifier
                    .animateItemPlacement()
                    .clip(Shapes.large)
                    .clickable { onBottomItemClicked(item) },
                appName = item.appName,
                icon = item.appIcon.icon,
                contentColor = contentColor
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardDoubleArrowUp,
                    contentDescription = "Move Up",
                    tint = contentColor
                )
            }
        }
    }
}
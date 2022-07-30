package work.racka.reluct.android.screens.screentime.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.model.domain.app_info.AppInfo

@Composable
fun ManageAppsDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    isLoading: Boolean,
    topItemsHeading: String,
    bottomItemsHeading: String,
    topItems: List<AppInfo>,
    bottomItems: List<AppInfo>,
    onTopItemClicked: (app: AppInfo) -> Unit,
    onBottomItemClicked: (app: AppInfo) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = modifier,
            shape = Shapes.large,
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(Dimens.MediumPadding.size),
                verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ManageAppsUI(
                    modifier = Modifier.height(480.dp),
                    isLoading = isLoading,
                    topItemsHeading = topItemsHeading,
                    bottomItemsHeading = bottomItemsHeading,
                    topItems = topItems,
                    bottomItems = bottomItems,
                    onTopItemClicked = onTopItemClicked,
                    onBottomItemClicked = onBottomItemClicked,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    ReluctButton(
                        buttonText = stringResource(id = R.string.ok),
                        icon = Icons.Rounded.Done,
                        onButtonClicked = onDismiss
                    )
                }
            }
        }
    }
}

/** This is a LazyColum, be careful when nesting it inside other Columns **/
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ManageAppsUI(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    isLoading: Boolean,
    topItemsHeading: String,
    bottomItemsHeading: String,
    topItems: List<AppInfo>,
    bottomItems: List<AppInfo>,
    onTopItemClicked: (app: AppInfo) -> Unit,
    onBottomItemClicked: (app: AppInfo) -> Unit,
    contentColor: Color = LocalContentColor.current,
    headerTonalElevation: Dp = 0.dp
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        stickyHeader {
            ListGroupHeadingHeader(
                text = topItemsHeading,
                contentColor = contentColor,
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = headerTonalElevation
            )
        }

        if (isLoading) {
            item {
                LinearProgressIndicator(Modifier.padding(Dimens.LargePadding.size))
            }
        } else if (topItems.isEmpty()) {
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
            ListGroupHeadingHeader(
                text = bottomItemsHeading,
                contentColor = contentColor,
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = headerTonalElevation
            )
        }

        if (isLoading) {
            item {
                LinearProgressIndicator(Modifier.padding(Dimens.LargePadding.size))
            }
        } else if (bottomItems.isEmpty()) {
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
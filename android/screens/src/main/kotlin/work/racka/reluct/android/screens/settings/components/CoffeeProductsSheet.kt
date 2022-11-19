package work.racka.reluct.android.screens.settings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.bottomSheet.TopSheetSection
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.cardWithActions.ReluctDescriptionCard
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.billing.products.Product
import work.racka.reluct.common.features.settings.states.CoffeeProductsState
import work.racka.reluct.compose.common.theme.Dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CoffeeProductsSheet(
    state: CoffeeProductsState,
    onReloadProducts: () -> Unit,
    onPurchaseProduct: (Product) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    tonalElevation: Dp = 6.dp,
    shape: Shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
) {
    Surface(
        modifier = modifier,
        tonalElevation = tonalElevation,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = shape
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        ) {
            stickyHeader {
                TopSheetSection(
                    sheetTitle = stringResource(id = R.string.buy_me_coffee_text),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    onCloseClicked = onClose
                )
            }

            when (state) {
                is CoffeeProductsState.Loading -> {
                    item {
                        CircularProgressIndicator(Modifier.padding(Dimens.MediumPadding.size))
                    }
                }
                is CoffeeProductsState.FetchError -> {
                    item {
                        LottieAnimationWithDescription(
                            lottieResId = R.raw.error_occurred,
                            imageSize = 150.dp,
                            description = state.message
                        )
                    }

                    item {
                        ReluctButton(
                            buttonText = stringResource(id = R.string.retry_text),
                            icon = Icons.Rounded.Refresh,
                            onButtonClicked = onReloadProducts
                        )
                    }
                }
                is CoffeeProductsState.ShowProducts -> {
                    if (state.products.isEmpty()) {
                        item {
                            LottieAnimationWithDescription(
                                lottieResId = R.raw.no_data,
                                imageSize = 150.dp,
                                description = stringResource(id = R.string.no_products_text)
                            )
                        }
                    } else {
                        items(state.products) { item ->
                            ProductCard(
                                product = item,
                                icon = Icons.Rounded.Coffee,
                                onClick = { onPurchaseProduct(item) }
                            )
                        }
                    }
                }
                is CoffeeProductsState.PurchaseSuccess -> {
                    item {
                        LottieAnimationWithDescription(
                            lottieResId = R.raw.pay_success,
                            imageSize = 150.dp,
                            description = stringResource(id = R.string.thank_you_text)
                        )
                    }

                    item {
                        Text(
                            text = state.product.name,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                        Text(
                            text = state.product.price,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    item {
                        ReluctButton(
                            buttonText = stringResource(id = R.string.ok),
                            icon = Icons.Rounded.CheckCircle,
                            onButtonClicked = onClose
                        )
                    }
                }
                is CoffeeProductsState.PurchaseError -> {
                    item {
                        LottieAnimationWithDescription(
                            lottieResId = R.raw.payment_failed,
                            imageSize = 150.dp,
                            description = state.message
                        )
                    }

                    item {
                        ReluctButton(
                            buttonText = stringResource(id = R.string.retry_text),
                            icon = Icons.Rounded.Refresh,
                            onButtonClicked = { onPurchaseProduct(state.product) }
                        )
                    }
                }
            }

            // Bottom Space
            item {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
internal fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    ReluctDescriptionCard(
        modifier = modifier,
        title = {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge,
                color = LocalContentColor.current
            )
        },
        description = {
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyLarge,
                color = LocalContentColor.current.copy(alpha = .8f)
            )
            Text(
                text = stringResource(id = R.string.price_text_value, product.price),
                style = MaterialTheme.typography.titleLarge,
                color = LocalContentColor.current
            )
        },
        leftItems = { icon?.let { Icon(imageVector = it, contentDescription = null) } },
        rightItems = {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = "Open"
            )
        },
        onClick = onClick
    )
}

package work.racka.reluct.compose.common.components.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource

@Composable
actual fun stringResource(resource: StringResource): String =
    stringResource(id = resource.resourceId)

@Composable
actual fun stringResource(resource: StringResource, vararg formatArgs: Any): String =
    stringResource(id = resource.resourceId, *formatArgs)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun pluralStringResource(resource: PluralsResource, quantity: Int): String =
    pluralStringResource(id = resource.resourceId, count = quantity)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun pluralStringResource(
    resource: PluralsResource,
    quantity: Int,
    vararg formatArgs: Any
): String = pluralStringResource(id = resource.resourceId, count = quantity, *formatArgs)

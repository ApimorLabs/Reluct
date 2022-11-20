package work.racka.reluct.compose.common.components.resources

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource

@Composable
expect fun stringResource(resource: StringResource): String

@Composable
expect fun stringResource(resource: StringResource, vararg formatArgs: Any): String

@Composable
expect fun pluralStringResource(resource: PluralsResource, quantity: Int): String

@Composable
expect fun pluralStringResource(
    resource: PluralsResource,
    quantity: Int,
    vararg formatArgs: Any
): String

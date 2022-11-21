package work.racka.reluct.compose.common.components.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Plural
import dev.icerock.moko.resources.desc.PluralFormatted
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format

@Composable
actual fun stringResource(resource: StringResource): String = resource.localized()

@Composable
actual fun stringResource(resource: StringResource, vararg formatArgs: Any): String =
    remember(*formatArgs) {
        resource.format(*formatArgs).localized()
    }

@Composable
actual fun pluralStringResource(resource: PluralsResource, quantity: Int): String =
    remember(quantity) { StringDesc.Plural(resource, quantity) }.localized()

@Composable
actual fun pluralStringResource(
    resource: PluralsResource,
    quantity: Int,
    vararg formatArgs: Any
): String = remember(quantity, *formatArgs) {
    StringDesc.PluralFormatted(
        resource,
        quantity,
        *formatArgs
    )
}.localized()

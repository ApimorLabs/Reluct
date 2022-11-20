package work.racka.reluct.compose.common.components
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.desc.Plural
import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format

fun test() {
    val resource = SharedRes.strings.app_name
    val formatted = StringDesc.Raw("")
    val desc = StringDesc.ResourceFormatted(resource, "hello")
    val file = SharedRes.files.error_occurred
    val mFile = file.rawResId
    val asset = SharedRes.assets.no_tasks
    val mAsset = asset.path
}

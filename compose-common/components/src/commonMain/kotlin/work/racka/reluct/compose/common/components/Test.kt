package work.racka.reluct.compose.common.components

import dev.icerock.moko.resources.format

fun test() {
    val resource = SharedRes.strings.app_name
    val formatted = resource.format()
}

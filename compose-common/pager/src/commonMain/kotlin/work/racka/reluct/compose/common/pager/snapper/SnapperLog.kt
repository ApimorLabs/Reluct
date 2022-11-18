/**
 * Code from: https://github.com/chrisbanes/snapper
 */

package work.racka.reluct.compose.common.pager.snapper

internal object SnapperLog {
    const val debug = false
    inline fun d(log: () -> String) {
        if (debug) {
            log()
        }
    }
}

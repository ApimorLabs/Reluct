package work.racka.reluct

import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.win32.StdCallLibrary
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlin.jvm.optionals.getOrNull

class ProcessesProvider {


    init {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            flow<Int> {
                var run = 0
                while (run < 5) {
                    WinSysUser32HWND.run()
                    emit(run)
                    delay(2000L)
                    run++
                }
            }.collectLatest {  }
        }.invokeOnCompletion {
            scope.cancel()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getProcesses(): List<ProcessHandle> = ProcessHandle.allProcesses()
        .filter { it.parent().isPresent }
        .map { it.parent().getOrNull() }
        .toList()
        .mapNotNull { it }
        .distinctBy { it.info().command().getOrNull() }
}

object WinSysUser32HWND {
    @JvmStatic
    fun run() {
        val buffer = CharArray(1024 * 2)
        User32.INSTANCE.GetWindowTextW(User32.INSTANCE.GetForegroundWindow(), buffer, 1024)
        println("Active window title: " + Native.toString(buffer))
    }

    interface User32 : StdCallLibrary {
        fun GetForegroundWindow(): WinDef.HWND?
        fun GetWindowThreadProcessId()
        fun GetWindowTextW(hWnd: WinDef.HWND?, lpString: CharArray?, nMaxCount: Int)
        companion object {
            val INSTANCE = Native.loadLibrary(
                "user32",
                User32::class.java
            ) as User32
        }
    }
}
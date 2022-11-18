package work.racka.reluct

import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.ptr.IntByReference
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
                while (run < 10) {
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
        val lpdw = IntByReference(2)
        val user32 = User32.INSTANCE
        user32.GetWindowTextW(user32.GetForegroundWindow(), buffer, 1024)
        val processId = user32.GetWindowThreadProcessId(user32.GetForegroundWindow(), lpdw)
        println("Active window title: " + Native.toString(buffer))
        println("Thread ID: $processId")
    }

    interface User32 : StdCallLibrary {
        fun GetForegroundWindow(): WinDef.HWND?
        fun GetWindowThreadProcessId(hWnd: WinDef.HWND?, lpdwProcessId: IntByReference?): Int
        fun GetWindowTextW(hWnd: WinDef.HWND?, lpString: CharArray?, nMaxCount: Int)
        companion object {
            val INSTANCE = Native.load(
                "user32",
                User32::class.java
            ) as User32
        }
    }
}
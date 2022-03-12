package work.racka.reluct

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import work.racka.reluct.common.model.util.time.TimeUtils

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            val text = remember {
                mutableStateOf("2010-06-01T22:08:01")
            }
            val ans = remember { mutableStateOf("Nothing") }
            Column {
                TextField(
                    value = text.value,
                    onValueChange = {
                        text.value = it
                    }
                )
                Spacer(Modifier.height(8.dp))
                Text(ans.value)
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        ans.value = TimeUtils
                            .getTimeLeftFromLocalDateTime(
                                dateTime = text.value,

                                )
                    }
                ) {
                    Text("Compute")
                }

            }
        }
    }
}